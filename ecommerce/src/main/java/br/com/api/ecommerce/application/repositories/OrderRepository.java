package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.enums.OrderStatus;
import br.com.api.ecommerce.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepository {
    Page<Order> findOrdersByUserId(UUID userId, Pageable pageable);
    boolean existsByUserIdAndProductIdAndStatus(UUID userId, UUID productId, OrderStatus status);
}
