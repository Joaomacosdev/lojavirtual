package br.com.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "br.com.lojavirtual.model")
@ComponentScan(basePackages = "br.com.lojavirtual")
@EnableJpaRepositories(basePackages = "br.com.lojavirtual.repository")
@EnableTransactionManagement
public class LojavirtualApplication {


    public static void main(String[] args) {


        SpringApplication.run(LojavirtualApplication.class, args);

        System.out.println("-------------------------------");
        System.out.println(new BCryptPasswordEncoder().encode("123"));

    }

}
