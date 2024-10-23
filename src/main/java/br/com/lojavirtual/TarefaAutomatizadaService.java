package br.com.lojavirtual;

import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.UsuarioRepository;
import br.com.lojavirtual.service.ServiceSendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class TarefaAutomatizadaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    //@Scheduled(initialDelay = 2000, fixedDelay = 86400000)
    @Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") // Vai rodar todos os dias as 11 horas da manhã horario de São Paulo
    public void notificarUserTrocaSenha() throws MessagingException, UnsupportedEncodingException, InterruptedException {

        List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();



        for (Usuario usuario: usuarios){

            StringBuilder msg = new StringBuilder();
            msg.append("Olá, ").append(usuario.getPessoa()).append("<br/>");
            msg.append("Está na hora de trocar sua senha, já passou de 90 dias de validade").append("<br/>");
            msg.append("Troque sua senha a loja virtual da Loja virtual");

            serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());

            Thread.sleep(3000);
        }
    }
}
