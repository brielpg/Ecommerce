package br.com.api.ecommerce.services;

import br.com.api.ecommerce.core.exceptions.BadRequestException;
import br.com.api.ecommerce.core.models.Cart;
import br.com.api.ecommerce.core.models.Item;
import br.com.api.ecommerce.core.models.Order;
import br.com.api.ecommerce.core.models.Product;
import br.com.api.ecommerce.core.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.infrastructure.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;
    private final ProductService productService;

    @Transactional
    public Item create(DtoItemRequest itemRequest, Cart cart) {
        Product product = productService.getById(itemRequest.productId());

        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(itemRequest.quantity());
        item.setCart(cart);
        item.updateSubTotal();

        return item;
    }

    @Transactional
    public List<Item> createListOfItems(List<DtoItemRequest> itemsRequest, Order order){
        return itemsRequest.stream().map(dto -> {
            Product product = productService.getById(dto.productId());

            this.validateStock(product.getStock(), dto.quantity());

            Item item = new Item();
            item.setProduct(product);
            item.setQuantity(dto.quantity());
            item.setOrder(order);
            item.updateSubTotal();

            return item;
        }).toList();
    }

    private void validateStock(Integer stock, Integer quantity){
        if (stock < quantity)
            throw new BadRequestException("exception.item.quantity.not.available");
    }

    @Transactional
    public void updateItemsPrice(Product product) {
        List<Item> items = repository.findAllByProductId(product.getId());

        for (Item item : items) {
            item.updateSubTotal();
            item.getCart().recalculateTotal();
        }

        repository.saveAll(items);
    }
}
