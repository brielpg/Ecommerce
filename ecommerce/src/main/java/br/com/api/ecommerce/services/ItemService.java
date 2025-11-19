package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ProductService productService;

    @Transactional
    public Item create(DtoItemRequest itemRequest, Cart cart) {
        Item item = new Item();
        Product product = productService.getById(itemRequest.productId());

        item.setProduct(product);
        item.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        item.setQuantity(itemRequest.quantity());
        item.setCart(cart);

        return item;
    }

    @Transactional
    public List<Item> createListOfItems(List<DtoItemRequest> itemsRequest, Order order){
        return itemsRequest.stream().map(dto -> {
            Item item = new Item();
            Product product = productService.getById(dto.productId());

            this.validateStock(product.getStock(), dto.quantity());

            item.setProduct(product);
            item.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(dto.quantity())));
            item.setQuantity(dto.quantity());
            item.setOrder(order);

            return item;
        }).toList();
    }

    private void validateStock(Integer stock, Integer quantity){
        if (stock < quantity)
            throw new BadRequestException("exception.item.quantity.not.available");
    }
}
