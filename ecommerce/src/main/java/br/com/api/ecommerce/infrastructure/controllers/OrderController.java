package br.com.api.ecommerce.infrastructure.controllers;

import br.com.api.ecommerce.application.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.application.usecases.order.CreateOrderUseCase;
import br.com.api.ecommerce.application.usecases.order.GetOrderByIdUseCase;
import br.com.api.ecommerce.application.usecases.order.GetOrdersByUserUseCase;
import br.com.api.ecommerce.domain.models.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrdersByUserUseCase getOrdersByUserUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid OrderDtoCreate dto){
        Order order = createOrderUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Order>> getAllByUser(@PathVariable UUID userId, Pageable pageable){
        Page<Order> orders = getOrdersByUserUseCase.execute(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable UUID id){
        Order order = getOrderByIdUseCase.execute(id);
        return ResponseEntity.ok(order);
    }
}
