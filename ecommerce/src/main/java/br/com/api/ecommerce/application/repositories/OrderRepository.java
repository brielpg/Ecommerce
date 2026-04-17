package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.enums.OrderStatus;
import br.com.api.ecommerce.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);
    Page<Order> findOrdersByUserId(UUID userId, Pageable pageable);
    Order save(Order order);
    boolean existsByUserIdAndProductIdAndStatus(UUID userId, UUID productId, OrderStatus status);
}
