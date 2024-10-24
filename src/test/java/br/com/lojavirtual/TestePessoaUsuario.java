package br.com.lojavirtual;

import br.com.lojavirtual.controller.PessoaController;
import br.com.lojavirtual.enums.TipoEndereco;
import br.com.lojavirtual.model.Endereco;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.service.PessoaUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TestePessoaUsuario {

    @Autowired
    private PessoaController pessoaController;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testCadPessoaJuridica() throws ExceptionLojaVirtual {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNome("Alex fernando");
        pessoaJuridica.setEmail("joamarcosdev@gmail.com");
        pessoaJuridica.setTelefone("45999795800");
        pessoaJuridica.setInscEstadual("65556565656665");
        pessoaJuridica.setInscMunicipal("55554565656565");
        pessoaJuridica.setNomeFantasia("Nome Fantasia Exemplo");
        pessoaJuridica.setRazaoSocial("4656656566");

        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Jd Dias");
        endereco1.setCep("556556565");
        endereco1.setComplemento("Casa cinza");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setNumero("389");
        endereco1.setPessoa(pessoaJuridica);
        endereco1.setRuaLogra("Av. são joao sexto");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setUf("PR");
        endereco1.setCidade("Curitiba");


        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Jd Maracana");
        endereco2.setCep("7878778");
        endereco2.setComplemento("Andar 4");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setNumero("555");
        endereco2.setPessoa(pessoaJuridica);
        endereco2.setRuaLogra("Av. maringá");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setUf("PR");
        endereco2.setCidade("Curitiba");

        pessoaJuridica.getEnderecos().add(endereco2);
        pessoaJuridica.getEnderecos().add(endereco1);

        pessoaController.salvarPj(pessoaJuridica);

        for (Endereco endereco : pessoaJuridica.getEnderecos()){
            assertEquals(true, endereco.getId() > 0);
            assertEquals(true, pessoaJuridica.getId() > 0);

       }

        assertEquals(2, pessoaJuridica.getEnderecos().size());

    }

    @Test
    public void testCadPessoaFisica() throws ExceptionLojaVirtual {
        PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("12.345.678/0001-99");

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setCpf("73555183567");
        pessoaFisica.setNome("Alex fernando");
        pessoaFisica.setEmail("alex.fe85549989r9559nando.egidio@gmail.com");
        pessoaFisica.setTelefone("45999795800");
        pessoaFisica.setEmpresa(pessoaJuridica);
        try {
            pessoaFisica.setDataNascimento(formato.parse("2005-10-24"));
        } catch (ParseException e) {
            e.printStackTrace(); // Tratamento da exceção, se necessário
        }

        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Jd Dias");
        endereco1.setCep("556556565");
        endereco1.setComplemento("Casa cinza");
        endereco1.setNumero("389");
        endereco1.setPessoa(pessoaFisica);
        endereco1.setRuaLogra("Av. são joao sexto");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setUf("PR");
        endereco1.setCidade("Curitiba");
        endereco1.setEmpresa(pessoaJuridica);


        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Jd Maracana");
        endereco2.setCep("7878778");
        endereco2.setComplemento("Andar 4");
        endereco2.setNumero("555");
        endereco2.setPessoa(pessoaFisica);
        endereco2.setRuaLogra("Av. maringá");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setUf("PR");
        endereco2.setCidade("Curitiba");
        endereco2.setEmpresa(pessoaJuridica);

        pessoaFisica.getEnderecos().add(endereco2);
        pessoaFisica.getEnderecos().add(endereco1);


        pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();

        assertEquals(true, pessoaFisica.getId() > 0 );

        for (Endereco endereco : pessoaFisica.getEnderecos()) {
            assertEquals(true, endereco.getId() > 0);
        }

        assertEquals(2, pessoaFisica.getEnderecos().size());

    }
}
