package br.com.api.ecommerce.application.usecases.cart;

import br.com.api.ecommerce.application.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.application.repositories.CartRepository;
import br.com.api.ecommerce.domain.models.Cart;
import br.com.api.ecommerce.domain.models.Item;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AddItemsToCartUseCase {
    private final CartRepository repository;
    private final GetCartByUserIdUseCase getCartByUserIdUseCase;

    public void execute(UUID userId, List<DtoItemRequest> itemsDto) {
        Cart cart = getCartByUserIdUseCase.execute(userId);

        for (DtoItemRequest itemRequest : itemsDto) {
            Item item = itemService.create(itemRequest, cart);
            cart.addItem(item);
        }

        repository.save(cart);
    }
}
