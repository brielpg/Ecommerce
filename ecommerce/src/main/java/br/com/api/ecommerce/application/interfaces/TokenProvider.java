package br.com.api.ecommerce.application.interfaces;

import br.com.api.ecommerce.domain.models.User;

public interface TokenProvider {
    String generateToken(User user);
    String validateToken(String token);
}
