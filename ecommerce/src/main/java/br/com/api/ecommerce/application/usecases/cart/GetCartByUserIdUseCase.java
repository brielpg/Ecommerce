package br.com.api.ecommerce.application.usecases.cart;

import br.com.api.ecommerce.application.interfaces.AuthorizationProvider;
import br.com.api.ecommerce.application.repositories.CartRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Cart;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetCartByUserIdUseCase {
    private final CartRepository repository;
    private final AuthorizationProvider authorization;

    public Cart execute(UUID userId) {
        if (authorization.validateAdminUser()) {
            throw new
        }

        return repository.findCartByUserId(userId)
                .orElseThrow(() -> new NotFoundException("exception.cart.not.found"));
    }
}
