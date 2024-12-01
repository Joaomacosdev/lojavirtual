package br.com.lojavirtual.repository;

import br.com.lojavirtual.model.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {
}
