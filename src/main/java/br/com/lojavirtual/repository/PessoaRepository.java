package br.com.lojavirtual.repository;

import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaJuridica, Long> {

    @Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
    public List<PessoaJuridica> pesquisaPorNomePJ(String nome);

    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public PessoaJuridica existeCnpjCadastrado(String cnpj);

    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public List<PessoaJuridica> existeCnpjCadastradoList(String cnpj);

    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public PessoaFisica existeCpfCadastrado(String cpf);

    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public List<PessoaFisica> existeCpfCadastradoList(String cpf);

    @Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
    public PessoaJuridica existeinscEstadualCadastrado(String inscEstadual);

    @Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
    public List<PessoaJuridica> existeinscEstadualCadastradoList(String inscEstadual);



}
