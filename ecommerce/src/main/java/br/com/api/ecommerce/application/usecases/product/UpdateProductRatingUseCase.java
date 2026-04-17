package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateProductRatingUseCase {
    private final ProductRepository repository;
    private final GetProductByIdUseCase getProductByIdUseCase;

    public void execute(UUID productId) {
        Product product = getProductByIdUseCase.execute(productId);
        Optional<Double> averageRating = repository.findAverageRatingByProductId(productId);

        product.setRating(averageRating.orElse(0.0));

        repository.save(product);
    }
}
