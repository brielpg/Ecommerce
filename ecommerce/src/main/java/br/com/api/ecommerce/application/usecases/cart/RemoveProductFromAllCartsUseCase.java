package br.com.api.ecommerce.application.usecases.cart;

import br.com.api.ecommerce.application.repositories.CartRepository;
import br.com.api.ecommerce.domain.models.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class RemoveProductFromAllCartsUseCase {
    private final CartRepository repository;

    public Cart execute(UUID userId) {
        List<Cart> carts = repository.findCartsContainingProduct(productId);

        for (Cart cart : carts) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cart.recalculateTotal();
            repository.save(cart);
        }

        log.debug("Removido o produto [{}] de todos os carrinhos", productId);
    }
}
