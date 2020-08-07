package br.com.ztech.controller.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class TransferenciaContaRequest implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7289874194846403307L;
	
	@NotNull
	private Integer agencia;
	
	@NotNull
	private Integer conta;
	
	@NotNull
	private Double valor;
	

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getAgencia() {
		return agencia;
	}

	public void setAgencia(Integer agencia) {
		this.agencia = agencia;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}
}