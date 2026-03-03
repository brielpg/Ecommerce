package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private ProductService productService;

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
