package br.com.lojavirtual.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@Component
public class JWTautenticacaoService {

    private static final Instant EXPIRATION_TIME = LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.of("-03:00"));

    private static final String SECRET = "secret";

    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    public void addAuthentication(HttpServletResponse response, String username) throws Exception{

        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();


        String token = TOKEN_PREFIX + JWT;

        response.addHeader(HEADER_STRING, token);

        response.getWriter().write("{\"Authorization\": " + token + "\"}");
    }
}
