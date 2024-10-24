package br.com.lojavirtual.repository;

import br.com.lojavirtual.model.Pessoa;
import br.com.lojavirtual.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<Pessoa, Long> {



    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public PessoaJuridica existeCpfCadastrado(String cpf);





}
