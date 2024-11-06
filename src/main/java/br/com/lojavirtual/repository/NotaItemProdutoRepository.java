package br.com.lojavirtual.repository;

import br.com.lojavirtual.model.NotaItemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

    @Query("select a from NotaItemProduto  a where a.produto.id = ?1 and a.notaFiscalCompra.id = ?2")
    List<NotaItemProduto> buscaNotaItemProdutoPorProdutoNota(Long idProduto, Long idNotaFiscal);

    @Query("select a from NotaItemProduto  a where a.produto.id = ?1")
    List<NotaItemProduto> buscaNotaItemProdutoPorId(Long idProduto);

    @Query("select a from NotaItemProduto a where a.notaFiscalCompra.id = ?1")
    List<NotaItemProduto> buscaNotaItemProdutoPorNotaFiscal(Long notaFiscal);

    @Query("select  a from NotaItemProduto  a where a.empresa.id = ?1")
    List<NotaItemProduto> buscaNotaItemProdutoPorEmpresa(Long idEmpresa);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from nota_item_produto where id = ?1")
    void deleteByIdNotaItem(Long id);
}
