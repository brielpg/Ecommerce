package br.com.api.ecommerce.services;


import br.com.api.ecommerce.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    @Value("${api.security.token.expiration-hours}")
    private Long expirationHours;

    @Value("${api.security.token.timezone}")
    private String timezone;


    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException ex){
            throw new RuntimeException("Error while generating token ", ex);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex){
            throw new RuntimeException("Invalid token ", ex);
        }
    }

    private Instant genExpirationDate() {
        ZoneId zoneId = timezone.equals("default") ? ZoneId.systemDefault() : ZoneId.of(timezone);

        return LocalDateTime.now(zoneId).plusHours(expirationHours).atZone(zoneId).toInstant();
    }
}
