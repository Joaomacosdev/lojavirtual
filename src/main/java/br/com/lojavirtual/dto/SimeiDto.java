package br.com.lojavirtual.dto;

import java.io.Serializable;

public class SimeiDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean optante;
    private String data_opcao;
    private String data_exclusao;
    private String ultima_atualizacao;

    public boolean isOptante() {
        return optante;
    }

    public void setOptante(boolean optante) {
        this.optante = optante;
    }

    public String getData_opcao() {
        return data_opcao;
    }

    public void setData_opcao(String data_opcao) {
        this.data_opcao = data_opcao;
    }

    public String getData_exclusao() {
        return data_exclusao;
    }

    public void setData_exclusao(String data_exclusao) {
        this.data_exclusao = data_exclusao;
    }

    public String getUltima_atualizacao() {
        return ultima_atualizacao;
    }

    public void setUltima_atualizacao(String ultima_atualizacao) {
        this.ultima_atualizacao = ultima_atualizacao;
    }
}
