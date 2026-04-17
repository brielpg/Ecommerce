package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RestoreProductUseCase {
    private final ProductRepository repository;
    private final GetProductByIdUseCase getProductByIdUseCase;

    public void execute(UUID id) {
        Product product = getProductByIdUseCase.execute(id);
        product.setActive(true);
        repository.save(product);
    }
}
