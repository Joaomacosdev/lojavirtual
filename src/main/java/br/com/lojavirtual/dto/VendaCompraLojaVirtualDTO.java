package br.com.lojavirtual.dto;

import br.com.lojavirtual.model.Endereco;
import br.com.lojavirtual.model.Pessoa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendaCompraLojaVirtualDTO {

    private Long id;

    private BigDecimal valorTotal;

    private Pessoa pessoa;

    private Endereco cobranca;

    private Endereco entrega;

    private BigDecimal valorDesc;

    private BigDecimal valorFret;

    private List<ItemVendaLojaDTO> itemVendaLojas = new ArrayList<ItemVendaLojaDTO>();

    public List<ItemVendaLojaDTO> getItemVendaLojas() {
        return itemVendaLojas;
    }

    public void setItemVendaLojas(List<ItemVendaLojaDTO> itemVendaLojas) {
        this.itemVendaLojas = itemVendaLojas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorFret() {
        return valorFret;
    }

    public void setValorFret(BigDecimal valorFret) {
        this.valorFret = valorFret;
    }

    public BigDecimal getValorDesc() {
        return valorDesc;
    }

    public void setValorDesc(BigDecimal valorDesc) {
        this.valorDesc = valorDesc;
    }

    public Endereco getCobranca() {
        return cobranca;
    }

    public void setCobranca(Endereco cobranca) {
        this.cobranca = cobranca;
    }

    public Endereco getEntrega() {
        return entrega;
    }

    public void setEntrega(Endereco entrega) {
        this.entrega = entrega;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
