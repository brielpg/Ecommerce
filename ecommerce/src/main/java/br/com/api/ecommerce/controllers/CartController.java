package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService service;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getByUserId(@PathVariable UUID userId){
        Cart cart = service.getByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearUserCart(@PathVariable UUID userId){
        service.clearUserCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addItemsToUserCart(@PathVariable UUID userId, @RequestBody @Valid List<DtoItemRequest> dto){
        service.addItemsToUserCart(userId, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{userId}/items")
    public ResponseEntity<Void> removeItemsFromUserCart(@PathVariable UUID userId, @RequestBody List<DtoItemRequest> itemsToRemove){
        service.removeItemsFromUserCart(userId, itemsToRemove);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
