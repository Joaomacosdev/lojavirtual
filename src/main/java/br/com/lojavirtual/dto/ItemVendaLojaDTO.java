package br.com.lojavirtual.dto;

import br.com.lojavirtual.model.Produto;

public class ItemVendaLojaDTO {

    private Double quantidade;

    private Produto produto;

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
