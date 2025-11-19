package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.models.dtos.Item.ItemDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository repository;

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
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public Cart getByUserId(UUID userId) {
        return repository.findCartByUserId(userId)
                .orElseThrow(() -> new NotFoundException("exception.cart.not.found"));
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public void clearUserCart(UUID userId) {
        repository.deleteAllItemsFromUserCart(userId);
        repository.resetCartTotal(userId);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public void addItemsToUserCart(UUID userId, List<DtoItemRequest> itemsDto) {
        Cart cart = this.getByUserId(userId);

        BigDecimal totalPriceToAdd = BigDecimal.ZERO;

        for (DtoItemRequest itemRequest : itemsDto) {
            Item existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(itemRequest.productId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + itemRequest.quantity();
                existingItem.setQuantity(newQuantity);
                existingItem.setSubTotal(existingItem.getProduct().getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            } else {
                Item item = itemService.create(itemRequest, cart);
                cart.getItems().add(item);
            }
        }

        this.recalculateCartTotal(cart);
        this.save(cart);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public void removeItemsFromUserCart(UUID userId, List<DtoItemRequest> itemsToRemove) {
        if (itemsToRemove.isEmpty()) throw new BadRequestException("exception.cart.items.is.empty");

        Cart cart = this.getByUserId(userId);

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
            } else {
                // Otherwise, we reduce the amount
                int newQuantity = availableQuantity - quantityToRemove;
                itemToRemove.setQuantity(newQuantity);
                itemToRemove.setSubTotal(itemToRemove.getProduct().getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            }
        }

        this.recalculateCartTotal(cart);
        this.save(cart);
    }

    @Transactional
    public void removeProductFromAllCarts(UUID productId) {
        List<Cart> carts = repository.findCartsContainingProduct(productId);

        for (Cart cart : carts) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            this.recalculateCartTotal(cart);
            repository.save(cart);
        }
    }

    private void recalculateCartTotal(Cart cart) {
        BigDecimal newTotal = cart.getItems().stream()
                .map(Item::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(newTotal);
    }

    public CartDtoList entityToDto(Cart cart) {
        return new CartDtoList(
                cart.getId(),
                cart.getItems().stream()
                        .map(item -> new ItemDtoList(
                                item.getId(),
                                new ProductDtoList(
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getProduct().getDescription(),
                                        item.getProduct().getPrice(),
                                        item.getProduct().getStock()
                                ),
                                item.getQuantity(),
                                item.getSubTotal()
                        )).toList(),
                cart.getTotalPrice()
        );
    }
}
