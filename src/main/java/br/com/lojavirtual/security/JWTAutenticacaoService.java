package br.com.lojavirtual.security;

import br.com.lojavirtual.ApplicationContextLoad;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@Component
public class JWTAutenticacaoService {

    private static final Instant EXPIRATION_TIME = LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.of("-03:00"));

    private static final String SECRET = "secret";

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String HEADER_STRING = "Authorization";

    public void addAuthentication(HttpServletResponse response, String username) throws Exception{

        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();


        String token = TOKEN_PREFIX + JWT;

        response.addHeader(HEADER_STRING, token);

        liberacaoCors(response);

        response.getWriter().write("{\"Authorization\": " + token + "\"}");
    }

    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response){

        String token = request.getHeader(HEADER_STRING);

        if (token != null){
            String tokenLimpo = token.replace(TOKEN_PREFIX, "");

            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(tokenLimpo)
                    .getBody().getSubject();

            if (user != null){
                Usuario usuario = ApplicationContextLoad
                        .getApplicationContext()
                        .getBean(UsuarioRepository.class).findUserByLogin(user);

                if (usuario != null){
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            usuario.getSenha(),
                            usuario.getAuthorities());

                }
            }
        }

        liberacaoCors(response);
        return null;
    }

    private void liberacaoCors(HttpServletResponse response){
        if(response.getHeader("Access-Control-Allow_Origin") == null){
            response.addHeader("Access-Control-Allow_Origin", "*");
        }

        if(response.getHeader("Access-Control-Allow_Headers") == null){
            response.addHeader("Access-Control-Allow_Headers", "*");
        }

        if(response.getHeader("Access-Control-Request_Headers") == null){
            response.addHeader("Access-Control-Request_Headers", "*");
        }

        if(response.getHeader("Access-Control-Allow_Methods") == null){
            response.addHeader("Access-Control-Allow_Methods", "*");
        }
    }
}
