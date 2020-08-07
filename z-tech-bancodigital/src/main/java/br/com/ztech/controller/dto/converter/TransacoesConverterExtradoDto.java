package br.com.ztech.controller.dto.converter;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import br.com.ztech.controller.dto.ClienteDto;
import br.com.ztech.controller.dto.ContaDto;
import br.com.ztech.controller.dto.ExtratoDto;
import br.com.ztech.domain.Transacao;

@Configuration
public class TransacoesConverterExtradoDto implements Converter<List<Transacao>, ExtratoDto> {
	
	@Override
	public ExtratoDto convert(List<Transacao> source) {
		
		var conta = source.stream().findFirst().get().getConta();
		var cliente = conta.getCliente();
		
		ExtratoDto extratoDto = new ExtratoDto();
		extratoDto.setConta(new ContaDto());
		extratoDto.getConta().setAgencia(conta.getAgencia());
		extratoDto.getConta().setConta(conta.getNumeroConta());
		extratoDto.getConta().setCliente(new ClienteDto());
		extratoDto.getConta().getCliente().setCpf(cliente.getCpf());
		extratoDto.getConta().getCliente().setNome(cliente.getNome());
		
		extratoDto.setMovimentacao(null);
		
		return null;
	}
}