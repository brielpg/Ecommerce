package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid UserDtoCreate dto){
        User user = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(Pageable pageable){
        Page<User> users = service.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id){
        User user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody @Valid UserDtoUpdate dto){
        User user = service.update(dto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @GetMapping("/{id}/orders")
//    private ResponseEntity<Page<Order>> getOrdersByUser(@PathVariable UUID id, Pageable pageable){
//        Page<Order> orders = service.getOrdersByUser(id, pageable);
//        return ResponseEntity.ok(orders);
//    }
//
//    @GetMapping("/{id}/cart")
//    private ResponseEntity<Cart> getCartByUser(@PathVariable UUID id){
//        Cart cart = service.getCartByUser(id);
//        return ResponseEntity.ok(cart);
//    }
}
