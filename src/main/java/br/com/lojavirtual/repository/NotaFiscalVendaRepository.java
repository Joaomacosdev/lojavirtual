package br.com.lojavirtual.repository;


import br.com.lojavirtual.model.NotaFiscalVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

    @Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLojaVirtual.id = ?1")
    List<NotaFiscalVenda> buscaNotaPorVenda(Long idVenda);

    @Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLojaVirtual.id = ?1")
    NotaFiscalVenda buscaNotaPorVendaUnica(Long idVenda);
}
