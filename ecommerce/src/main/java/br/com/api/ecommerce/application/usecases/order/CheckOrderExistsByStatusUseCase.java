package br.com.api.ecommerce.application.usecases.order;

import br.com.api.ecommerce.application.repositories.OrderRepository;
import br.com.api.ecommerce.domain.enums.OrderStatus;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CheckOrderExistsByStatusUseCase {
    private final OrderRepository repository;

    public boolean execute(UUID userId, UUID productId, OrderStatus status) {
        return repository.existsByUserIdAndProductIdAndStatus(userId, productId, status);
    }
}
