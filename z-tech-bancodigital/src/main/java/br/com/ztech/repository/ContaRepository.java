package br.com.ztech.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ztech.domain.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	Optional<Conta> findByAgenciaAndConta(Integer agencia, Integer conta);

}