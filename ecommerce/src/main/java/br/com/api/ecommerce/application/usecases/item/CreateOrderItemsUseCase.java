package br.com.api.ecommerce.application.usecases.item;

import br.com.api.ecommerce.application.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.application.usecases.product.GetProductByIdUseCase;
import br.com.api.ecommerce.domain.exceptions.BadRequestException;
import br.com.api.ecommerce.domain.models.Item;
import br.com.api.ecommerce.domain.models.Order;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateOrderItemsUseCase {
    private final GetProductByIdUseCase getProductByIdUseCase;

    public List<Item> execute(List<DtoItemRequest> itemsRequest, Order order) {
        return itemsRequest.stream().map(dto -> {
            Product product = getProductByIdUseCase.execute(dto.productId());

            if (product.getStock() < dto.quantity()) {
                throw new BadRequestException("exception.item.quantity.not.available");
            }

            Item item = new Item();
            item.setProduct(product);
            item.setQuantity(dto.quantity());
            item.setOrder(order);
            item.updateSubTotal();

            return item;
        }).toList();
    }
}
