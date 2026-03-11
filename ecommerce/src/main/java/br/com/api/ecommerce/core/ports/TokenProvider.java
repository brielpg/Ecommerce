package br.com.api.ecommerce.core.ports;

import br.com.api.ecommerce.core.models.User;

public interface TokenProvider {

    String generateToken(User user);

    String validateToken(String token);
}
