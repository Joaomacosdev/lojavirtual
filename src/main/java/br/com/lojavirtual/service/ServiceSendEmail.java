package br.com.lojavirtual.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class ServiceSendEmail {

    private String userName = "springjava91@gmail.com";
    private String senha = "u t w i t e a o b j b m g j p b";

    @Async
    public void enviarEmailHtml(String assunto, String menssagem, String emailDestinto) throws MessagingException, UnsupportedEncodingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls", "false");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication(){

                return new PasswordAuthentication(userName, senha);
            }

        });

        session.setDebug(true);

        Address[] toUser = InternetAddress.parse(emailDestinto);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName, "Java - Spring boot", "UTF-8"));
        message.setRecipients(Message.RecipientType.TO, toUser);
        message.setSubject(assunto);
        message.setText(menssagem);

        Transport.send(message);
    }
}
