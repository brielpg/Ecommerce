package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        item.setQuantity(itemRequest.quantity());
        item.setUnitPrice(product.getPrice());
        item.setCart(cart);

        return item;
    }

    @Transactional
    public List<Item> createListOfItems(List<DtoItemRequest> itemsRequest, Order order){
        return itemsRequest.stream().map(dto -> {
            Item item = new Item();
            Product product = productService.getById(dto.productId());
            item.setProduct(product);
            if (product.getStock() < dto.quantity()) throw new RuntimeException();
            item.setQuantity(dto.quantity());
            item.setUnitPrice(product.getPrice());
            item.setOrder(order);
            return item;
        }).toList();
    }
}
