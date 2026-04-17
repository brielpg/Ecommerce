package br.com.api.ecommerce.application.usecases.order;

import br.com.api.ecommerce.application.repositories.OrderRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Order;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetOrderByIdUseCase {
    private final OrderRepository repository;

    public Order execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.order.not.found"));
    }
}
