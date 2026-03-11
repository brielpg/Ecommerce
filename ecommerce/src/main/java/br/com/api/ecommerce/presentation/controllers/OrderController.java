package br.com.api.ecommerce.presentation.controllers;

import br.com.api.ecommerce.core.models.Order;
import br.com.api.ecommerce.core.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid OrderDtoCreate dto){
        Order order = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Order>> getAllByUser(@PathVariable UUID userId, Pageable pageable){
        Page<Order> orders = service.getAllByUser(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable UUID id){
        Order order = service.getById(id);
        return ResponseEntity.ok(order);
    }
}
