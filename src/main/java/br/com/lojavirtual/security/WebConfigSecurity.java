package br.com.lojavirtual.security;

import javax.servlet.http.HttpSessionListener;

import br.com.lojavirtual.service.ImplementacaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

			http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					.disable().authorizeRequests().antMatchers("/").permitAll()
					.antMatchers("/index").permitAll()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

					.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

					.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)

					.addFilterBefore(new JwtAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);


	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers(HttpMethod.GET, "/salvarAcesso", "/deleteAcesso")
		//.antMatchers(HttpMethod.POST, "/salvarAcesso", "/deleteAcesso");
		/*Ingnorando URL no momento para nao autenticar*/
	}

}
