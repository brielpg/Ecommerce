package br.com.api.ecommerce.application.usecases.cart;

import br.com.api.ecommerce.domain.models.Cart;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCartUseCase {

    public Cart execute(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }
}
