package br.com.api.ecommerce.application.usecases.item;

import br.com.api.ecommerce.application.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.application.usecases.product.GetProductByIdUseCase;
import br.com.api.ecommerce.domain.models.Cart;
import br.com.api.ecommerce.domain.models.Item;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateItemUseCase {
    private final GetProductByIdUseCase getProductByIdUseCase;

    public Item execute(DtoItemRequest itemRequest, Cart cart) {
        Product product = getProductByIdUseCase.execute(itemRequest.productId());

        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(itemRequest.quantity());
        item.setCart(cart);
        item.updateSubTotal();

        return item;
    }
}
