package br.com.ztech.controller.dto;

import java.util.List;

public class ExtratoDto {
	
	private ContaDto conta;
	private List<TransacaoDto> movimentacao;
	
	public ContaDto getConta() {
		return conta;
	}
	public void setConta(ContaDto conta) {
		this.conta = conta;
	}
	public List<TransacaoDto> getMovimentacao() {
		return movimentacao;
	}
	public void setMovimentacao(List<TransacaoDto> movimentacao) {
		this.movimentacao = movimentacao;
	}
}	