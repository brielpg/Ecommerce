package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    @Autowired
    private ProductService productService;

    @Transactional
    public Item create(CartDtoItemRequest itemRequest, Cart cart) {
        Item item = new Item();
        Product product = productService.getById(itemRequest.productId());
        item.setProduct(product);
        item.setQuantity(itemRequest.quantity());
        item.setUnitPrice(product.getPrice());
        item.setCart(cart);

        return item;
    }
}
