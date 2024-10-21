package br.com.lojavirtual;

import br.com.lojavirtual.controller.PessoaController;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.service.PessoaUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TestePessoaUsuario {

    @Autowired
    private PessoaUserService pessoaUserService;


    @Autowired
    private PessoaController pessoaController;

    @Test
    public void testCadPessoaFisica() throws ExceptionLojaVirtual {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNome("Alex fernando");
        pessoaJuridica.setEmail("testesalvarpj@gmail.com");
        pessoaJuridica.setTelefone("45999795800");
        pessoaJuridica.setInscEstadual("65556565656665");
        pessoaJuridica.setInscMunicipal("55554565656565");
        pessoaJuridica.setNomeFantasia("Nome Fantasia Exemplo");
        pessoaJuridica.setRazaoSocial("4656656566");

        System.out.println("Pessoa Juridica: " + pessoaJuridica);


        pessoaController.salvarPj(pessoaJuridica);


    }
}
