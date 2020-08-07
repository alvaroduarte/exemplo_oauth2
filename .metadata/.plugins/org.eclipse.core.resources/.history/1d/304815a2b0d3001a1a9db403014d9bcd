package petz.com.br.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import petz.com.br.controller.CrudClienteController;
import petz.com.br.domain.Cliente;
import petz.com.br.repository.ClienteRepository;

@Service
public class ClienteService {
	
	private Logger logger = LogManager.getLogger(CrudClienteController.class);
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public List<Cliente> buscarTodos() {
		
		logger.info("buscarTodos");
		
		return clienteRepository.findAll();
		
	}
	
	public Cliente buscarPorCpf(Long cpf) {
		
		logger.info("buscarPorCpf {}", cpf);
		
		return clienteRepository.findByCpf(cpf);
	}
	
	public Cliente buscarPorId(Long id) {
		
		logger.info("buscarPorId {}", id);
			
		final Optional<Cliente> clienteOptional = clienteRepository.findById(id);
		
		if(clienteOptional.isPresent()) {
		
			final var cliente = clienteOptional.get();
			
			logger.debug("{} encontrado com sucesso!", cliente);
			
			return cliente;
			
		}
		
		logger.debug("Cliente id {} não encontrado na base de dados!", id);

		return null;
	}
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		
		logger.info("salvar {}", cliente);
		
		final var retorno = clienteRepository.save(cliente);
		
		logger.debug("{} Salvo com sucesso!", cliente);
		
		return retorno;
		
	}
	
	@Transactional
	public Boolean remover(Long id){
		
		logger.info("remover id {}", id);
		
		final var clienteOptional = clienteRepository.findById(id);
		
		if(clienteOptional.isPresent()) {
			
			logger.debug("removendo o cliente id {}", id);
			
			clienteRepository.deleteById(id);
			
			logger.debug("cliente id {} removido com sucesso", id);
			
			return Boolean.TRUE;
		}
		
		logger.debug("cliente id {} removido com sucesso", id);
		
		return Boolean.FALSE;
	}
}