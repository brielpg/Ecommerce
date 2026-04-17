package br.com.api.ecommerce.application.usecases.cart;

import br.com.api.ecommerce.application.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.application.repositories.CartRepository;
import br.com.api.ecommerce.domain.exceptions.BadRequestException;
import br.com.api.ecommerce.domain.models.Cart;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RemoveItemsFromCartUseCase {
    private final CartRepository repository;
    private final GetCartByUserIdUseCase getCartByUserIdUseCase;

    public void execute(UUID userId, List<DtoItemRequest> itemsToRemove) {
        if (itemsToRemove.isEmpty()) throw new BadRequestException("exception.cart.items.is.empty");

        Cart cart = getCartByUserIdUseCase.execute(userId);
        itemsToRemove.forEach(
                dto -> cart.removeItem(dto.productId(), dto.quantity())
        );

        repository.save(cart);
    }
}
