package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetProductByIdUseCase {
    private final ProductRepository repository;

    public Product execute(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.product.not.found"));
    }
}
