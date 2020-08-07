package br.com.ztech.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.ztech.domain.Cliente;
import br.com.ztech.domain.Conta;
import br.com.ztech.domain.TipoTransacao;
import br.com.ztech.domain.TipoTransacaoEnum;
import br.com.ztech.domain.Transacao;
import br.com.ztech.domain.ValoDonusBonusEnum;
import br.com.ztech.exception.ContaDestinoNotFoundException;
import br.com.ztech.exception.ContaNotFoundException;
import br.com.ztech.exception.CpfInvalidoException;
import br.com.ztech.exception.SaldoInsuficienteNotFoundException;
import br.com.ztech.repository.ContaRepository;
import br.com.ztech.repository.TransacaoRepository;

@Service
public class ContaService {

	private final static Logger logger = LogManager.getLogger(ContaService.class);

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private ClienteService clienteService;
	
	public Conta buscarConta(Integer agencia, Integer numeroConta) {
		
		logger.info("buscarConta {}, {}", agencia, numeroConta);
		
		return contaRepository.findByAgenciaAndConta(agencia, numeroConta).orElseThrow(() -> new ContaNotFoundException());
	}
	
	@Transactional
	public Conta abrirConta(Cliente cliente) {

		logger.info("abrirConta {}", cliente);
		
		final var cpf = cliente.getCpf();

		validaCpf(cpf);
		
		clienteService.verificaExisteContaAbertaCliente(cpf);
		
		Random random = new Random();
		final var conta = contaRepository.save(new Conta(100, random.nextInt(1000000), BigDecimal.ZERO, clienteService.salvar(cliente)));

		logger.info("Conta aberta com suecesso! {}", conta);

		return conta;
	}
	
	@Transactional
	public Conta depositarConta(Integer agencia, Integer numeroConta, BigDecimal valorTransacao) {
		
		logger.info("depositarConta agencia {}, numeroConta {}, valor {}", agencia, numeroConta, valorTransacao);
		
		final var conta = contaRepository.findByAgenciaAndConta(agencia, numeroConta).orElseThrow(() -> new ContaNotFoundException());
		
		logger.info("{}", conta);
				
		conta.setSaldo(valorTransacao.add(conta.getSaldo().multiply(ValoDonusBonusEnum.DONUS_BONUS.getValor())));
		
		final var retorno = contaRepository.save(conta);
		
		final var transacao = new Transacao(LocalDateTime.now(), valorTransacao, retorno, new TipoTransacao(TipoTransacaoEnum.DEPOSITO_DINHEIRO.getCodigo())); 
		
		transacaoRepository.save(transacao);
		
		logger.info("deposito efetuado com sucesso na {}, {}", retorno, transacao);
		
		return retorno;
	}
	
	@Transactional
	public Conta sacarConta(Integer agencia, Integer numeroConta, BigDecimal valorTransacao) {
		
		logger.info("sacarConta agencia {}, numeroConta {}, valor {}", agencia, numeroConta, valorTransacao);
		
		final var conta = contaRepository.findByAgenciaAndConta(agencia, numeroConta).orElseThrow(() -> new ContaNotFoundException());
		
		logger.info("{}", conta);
		
		var valorCustoRetiradaDinheiro = valorTransacao.multiply(ValoDonusBonusEnum.DONUS_CUSTO_RETIRADA_DINHEIRO.getValor()).divide(new BigDecimal(100));
		
		conta.setSaldo(conta.getSaldo().subtract(  valorTransacao   ).subtract( valorCustoRetiradaDinheiro ));
		
		validaValorSaldo(conta.getSaldo());
		
		final var retorno = contaRepository.save(conta);
		
		final var transacao = new Transacao(LocalDateTime.now(), valorTransacao, retorno, new TipoTransacao(TipoTransacaoEnum.RETIRAR_DINHEIRO.getCodigo())); 
		
		transacaoRepository.save(transacao);
		
		logger.info("deposito efetuado com sucesso na {}, {}", retorno, transacao);
		
		return retorno;
	}
	
	@Transactional
	public Conta transferir(Integer agencia, Integer numeroConta, BigDecimal valorTransacao, Integer agenciaDestino, Integer numeroContaDestino) {
		
		logger.info("transferir agencia {}, numeroConta {}, valor {}, agenciaDestino {}, numeroContaDestino {}", agencia, numeroConta, valorTransacao, agenciaDestino, numeroContaDestino);
		
		final var conta = contaRepository.findByAgenciaAndConta(agencia, numeroConta).orElseThrow(() -> new ContaNotFoundException());
		final var contaDestino = contaRepository.findByAgenciaAndConta(agencia, numeroConta).orElseThrow(() -> new ContaDestinoNotFoundException());
		
		conta.setSaldo(conta.getSaldo().subtract(valorTransacao));
		
		validaValorSaldo(conta.getSaldo());
		
		final var retornoConta = contaRepository.save(conta);
		
		contaDestino.setSaldo(contaDestino.getSaldo().add(valorTransacao));
		
		final var retornoContaDestino = contaRepository.save(contaDestino);
		
		final var transacaoConta = new Transacao(LocalDateTime.now(), valorTransacao, retornoConta, retornoContaDestino, new TipoTransacao(TipoTransacaoEnum.TRANSFERENCIA_DINHEIRO.getCodigo())); 
		
		final var transacaoContaDestino = new Transacao(LocalDateTime.now(), valorTransacao, retornoContaDestino, retornoConta, new TipoTransacao(TipoTransacaoEnum.ENTRADA_DINHEIRO_POR_TRANSFERENCIA.getCodigo()));
		
		transacaoRepository.saveAll(Arrays.asList( transacaoConta, transacaoContaDestino));
		
		logger.info("Transferencia efetuado com sucesso da {} para {} no valor de {}", retornoConta, retornoContaDestino, valorTransacao);
		
		return retornoConta;
	}
	
	public static void validaValorSaldo(BigDecimal valor) {
		
		if(valor.signum() < 0){
			
			throw new SaldoInsuficienteNotFoundException();
		}
		
	}

	public static void validaCpf(String cpf) { 
		
		logger.info("validaCpf {}", cpf);
		
		final var cpfValidator = new CPFValidator(); 
		
		final List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf); 
		
		if(erros.size() > 0) {
			
			logger.info("Cpf {} inválido", cpf);
			
			throw new CpfInvalidoException();
		}
		
		logger.info("Cpf {} valido", cpf);
	}
}