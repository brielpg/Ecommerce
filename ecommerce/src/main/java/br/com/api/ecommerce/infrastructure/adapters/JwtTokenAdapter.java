package br.com.api.ecommerce.infrastructure.adapters;

import br.com.api.ecommerce.core.models.User;
import br.com.api.ecommerce.core.ports.TokenProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JwtTokenAdapter implements TokenProvider {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    @Value("${api.security.token.expiration-hours}")
    private Long expirationHours;

    @Value("${api.security.token.timezone}")
    private String timezone;

    @Override
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withClaim("userId", String.valueOf(user.getId()))
                    .withClaim("role", String.valueOf(user.getRole()))
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException ex){
            throw new RuntimeException("Error while generating token ", ex);
        }
    }

    @Override
    public String validateToken(String token) {
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
