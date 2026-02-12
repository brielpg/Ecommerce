package br.com.api.ecommerce_email.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex){
            log.warn("Falha na verificação do token JWT", ex);
            return "";
        }
    }

    public String getRoleFromToken(String token) {
        String role = JWT.decode(token).getClaim("role").asString();
        log.debug("Role extraída do token: {}", role);
        return role;
    }
}
