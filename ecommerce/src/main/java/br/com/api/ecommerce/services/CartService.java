package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ItemService itemService;


    @Transactional
    public Cart create(User user){
        Cart cart = new Cart();
        cart.setUser(user);

        return cart;
    }

    @Transactional
    public void save(Cart cart){
        repository.save(cart);
    }

    @Transactional(readOnly = true)
    public Cart getByUserId(UUID userId) {
        return repository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("exception.cart.not.found"));
    }

    @Transactional
    public void clearUserCart(UUID userId) {
        Cart cart = this.getByUserId(userId);
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);

        this.save(cart);
    }

    @Transactional
    public void addItemsToUserCart(UUID userId, List<DtoItemRequest> itemsDto) {
        Cart cart = this.getByUserId(userId);

        BigDecimal totalPriceToAdd = BigDecimal.ZERO;

        for (DtoItemRequest itemRequest : itemsDto) {
            Item existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(itemRequest.productId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + itemRequest.quantity());
                totalPriceToAdd = totalPriceToAdd.add(existingItem.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            } else {
                Item item = itemService.create(itemRequest, cart);
                cart.getItems().add(item);
                totalPriceToAdd = totalPriceToAdd.add(item.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            }
        }

        cart.setTotalPrice(cart.getTotalPrice().add(totalPriceToAdd));
        this.save(cart);
    }

    @Transactional
    public void removeItemsFromUserCart(UUID userId, List<DtoItemRequest> itemsToRemove) {
        if (itemsToRemove.isEmpty()) throw new BadRequestException("exception.cart.items.is.empty");

        Cart cart = this.getByUserId(userId);
        BigDecimal totalPriceToRemove = BigDecimal.ZERO;

        for (DtoItemRequest itemRequest : itemsToRemove) {
            Item itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(itemRequest.productId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("exception.cart.product.not.in"));


            Integer availableQuantity = itemToRemove.getQuantity();
            Integer quantityToRemove = itemRequest.quantity();

            if (quantityToRemove > availableQuantity) throw new BadRequestException("exception.cart.quantity.not.available");

            if (quantityToRemove.equals(availableQuantity)) {
                // If the quantity to be removed is equal to the available one, we remove the item
                cart.getItems().remove(itemToRemove);
                totalPriceToRemove = totalPriceToRemove.add(itemToRemove.getUnitPrice().multiply(BigDecimal.valueOf(availableQuantity)));
            } else {
                // Otherwise, we reduce the amount
                itemToRemove.setQuantity(availableQuantity - quantityToRemove);
                totalPriceToRemove = totalPriceToRemove.add(itemToRemove.getUnitPrice().multiply(BigDecimal.valueOf(quantityToRemove)));
            }
        }

        cart.setTotalPrice(cart.getTotalPrice().subtract(totalPriceToRemove));
        this.save(cart);
    }
}
