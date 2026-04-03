package br.com.api.ecommerce.services;

import br.com.api.ecommerce.domain.exceptions.BadRequestException;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Cart;
import br.com.api.ecommerce.domain.models.Item;
import br.com.api.ecommerce.domain.models.User;
import br.com.api.ecommerce.application.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.infrastructure.repositories.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
        log.info("Limpando carrinho do usuário");
        repository.deleteAllItemsFromUserCart(userId);
        repository.resetCartTotal(userId);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public void addItemsToUserCart(UUID userId, List<DtoItemRequest> itemsDto) {
        Cart cart = this.getByUserId(userId);

        for (DtoItemRequest itemRequest : itemsDto) {
            Item item = itemService.create(itemRequest, cart);
            cart.addItem(item);
        }

        this.save(cart);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public void removeItemsFromUserCart(UUID userId, List<DtoItemRequest> itemsToRemove) {
        if (itemsToRemove.isEmpty()) throw new BadRequestException("exception.cart.items.is.empty");

        Cart cart = this.getByUserId(userId);
        itemsToRemove.forEach(dto -> cart.removeItem(dto.productId(), dto.quantity()));

        this.save(cart);
    }

    @Transactional
    public void removeProductFromAllCarts(UUID productId) {
        List<Cart> carts = repository.findCartsContainingProduct(productId);

        for (Cart cart : carts) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cart.recalculateTotal();
            repository.save(cart);
        }

        log.debug("Removido o produto [{}] de todos os carrinhos", productId);
    }
}
