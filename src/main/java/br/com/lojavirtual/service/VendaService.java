package br.com.lojavirtual.service;

import br.com.lojavirtual.model.VendaCompraLojaVirtual;
import br.com.lojavirtual.repository.VendaCompraLojaVirtualReposiory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class VendaService {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VendaCompraLojaVirtualReposiory vendaCompraLojaVirtualReposiory;


    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void exclusaoTotalVendaBanco2(Long idVenda) {
        String sql = "begin; update vd_cp_loja_virt set excluido = true where id = " + idVenda + "; commit;";
        jdbcTemplate.execute(sql);
    }


    public void exclusaoTotalVendaBanco(Long idVenda) {

        String value =
                " begin;"
                        + " UPDATE nota_fiscal_venda set venda_compra_loja_virt_id = null where venda_compra_loja_virt_id = " + idVenda + "; "
                        + " delete from nota_fiscal_venda where venda_compra_loja_virt_id = " + idVenda + "; "
                        + " delete from item_venda_loja where venda_compra_loja_virtu_id = " + idVenda + "; "
                        + " delete from status_rastreio where venda_compra_loja_virt_id = " + idVenda + "; "
                        + " delete from vd_cp_loja_virt where id = " + idVenda + "; "
                        + " commit; ";

        jdbcTemplate.execute(value);
    }


    public void ativaRegistroVendaBanco(Long idVenda) {
        String sql = "begin; update vd_cp_loja_virt set excluido = false where id = " + idVenda + "; commit;";
        jdbcTemplate.execute(sql);
        ;

    }

    @SuppressWarnings("unchecked")
    public List<VendaCompraLojaVirtual> consultaVendaFaixaData(String data1, String data2) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = dateFormat.parse(data1);
        Date date2 = dateFormat.parse(data2);

        return vendaCompraLojaVirtualReposiory.consultaVendaFaixaData(date1, date2);

    }


}
