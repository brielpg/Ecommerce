package br.com.api.ecommerce.application.usecases.order;

import br.com.api.ecommerce.application.repositories.OrderRepository;
import br.com.api.ecommerce.domain.models.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RequiredArgsConstructor
public class GetOrdersByUserUseCase {
    private final OrderRepository repository;

    public Page<Order> execute(UUID userId, Pageable pageable) {
        return repository.findOrdersByUserId(userId, pageable);
    }
}
