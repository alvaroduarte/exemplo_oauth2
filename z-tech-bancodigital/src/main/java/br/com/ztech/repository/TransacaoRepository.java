package br.com.ztech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ztech.domain.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
	
	Optional<List<Transacao>> findByContaAgenciaAndContaConta(Integer agencia, Integer conta);

}