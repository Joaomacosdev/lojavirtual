package br.com.lojavirtual.repository;

import br.com.lojavirtual.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @Query(value = "select a from ContaPagar a where upper(trim(a.descricao)) like %?1%")
    List<ContaPagar> buscaContaDesc(String desc);

    @Query(value = "select a from ContaPagar a where a.pessoa.id = ?1")
    List<ContaPagar> buscaContaPorPessoa(Long idPessoa);

    @Query(value = "select a from ContaPagar a where a.pessoa_fornecedor.id = ?1")
    List<ContaPagar> buscaContaPorFornecedor(Long idFornecedor);

    @Query(value = "select a from ContaPagar a where a.empresa.id = ?1")
    List<ContaPagar> buscaContaPorEmpresa(Long idEmpresa);
}
