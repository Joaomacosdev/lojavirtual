package br.com.lojavirtual;

import br.com.lojavirtual.util.ValidaCPF;
import br.com.lojavirtual.util.ValidaCnpj;

public class TesteCPFCNPJ {
    public static void main(String[] args) {
        boolean isCnpj = ValidaCnpj.isCNPJ("99.064.405/0001-50");

        System.out.println("Cnpj válido : " + isCnpj);

        boolean isCpf = ValidaCPF.isCPF("123.373.375-37");

        System.out.println("Cpf válido : " + isCpf);
    }
}
