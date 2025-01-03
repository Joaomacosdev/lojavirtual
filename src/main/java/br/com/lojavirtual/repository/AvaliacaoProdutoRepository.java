package br.com.lojavirtual.repository;

import br.com.lojavirtual.model.AvaliacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

    @Query(value = "select a from AvaliacaoProduto a where a.produto.id = ?1")
    List<AvaliacaoProduto> avaliacaoProduto(Long idProduto);

    @Query(value = "select a from AvaliacaoProduto a where a.produto.id = ?1 and a.pessoa.id = ?2")
    List<AvaliacaoProduto> avaliacaoProdutoPessoa(Long idProduto, Long idPessoa);

    @Query(value = "select a from AvaliacaoProduto a where a.pessoa.id = ?1 ")
    List<AvaliacaoProduto> avaliacaoProdutoPessoa(Long idPessoa);
}