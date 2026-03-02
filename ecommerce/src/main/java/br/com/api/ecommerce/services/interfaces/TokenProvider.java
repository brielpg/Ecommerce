package br.com.api.ecommerce.services.interfaces;

import br.com.api.ecommerce.models.User;

public interface TokenProvider {

    String generateToken(User user);

    String validateToken(String token);
}
