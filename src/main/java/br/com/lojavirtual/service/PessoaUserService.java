package br.com.lojavirtual.service;

import br.com.lojavirtual.dto.CepDto;
import br.com.lojavirtual.dto.ConsultaCnpjDto;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.PessoaFisicaRepository;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {
        //juridica = pessoaRepository.save(juridica);

        for (int i = 0; i < juridica.getEnderecos().size(); i++) {
            juridica.getEnderecos().get(i).setPessoa(juridica);
            juridica.getEnderecos().get(i).setEmpresa(juridica);
        }

        juridica = pessoaRepository.save(juridica);

        Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());

        if (usuarioPj == null) {

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
            }

            usuarioPj = new Usuario();
            usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPj.setEmpresa(juridica);
            usuarioPj.setPessoa(juridica);
            usuarioPj.setLogin(juridica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPj.setSenha(senhaCript);

            usuarioPj = usuarioRepository.save(usuarioPj);

            usuarioRepository.insereAcessoUser(usuarioPj.getId());
            usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");


            /*Fazer o envio de e-mail do login e da senha*/

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
            menssagemHtml.append("<b>Login: </b>" + juridica.getEmail() + "<br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");


            try {
                serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString(), juridica.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return juridica;
    }


    public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

        for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
            pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
            //pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
        }

        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

        Usuario usuarioPf = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        if (usuarioPf == null) {

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
            }

            usuarioPf = new Usuario();
            usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPf.setEmpresa(pessoaFisica);
            usuarioPf.setPessoa(pessoaFisica);
            usuarioPf.setLogin(pessoaFisica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPf.setSenha(senhaCript);

            usuarioPf = usuarioRepository.save(usuarioPf);

            usuarioRepository.insereAcessoUser(usuarioPf.getId());

            /*Fazer o envio de e-mail do login e da senha*/

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
            menssagemHtml.append("<b>Login: </b>" + pessoaFisica.getEmail() + "<br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");


            try {
                serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString(), pessoaFisica.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return pessoaFisica;
    }

    public CepDto consultaCep(String cep) {
        return new RestTemplate().getForEntity("https://viacep.com.br/ws/" + cep + "/json/", CepDto.class).getBody();
    }

    public ConsultaCnpjDto consultaCnpjReceitaWs(String cnpj){
        return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/" + cnpj, ConsultaCnpjDto.class).getBody();
    }
}
