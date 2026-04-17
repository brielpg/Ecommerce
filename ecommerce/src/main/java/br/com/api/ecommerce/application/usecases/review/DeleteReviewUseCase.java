package br.com.api.ecommerce.application.usecases.review;

import br.com.api.ecommerce.application.repositories.ReviewRepository;
import br.com.api.ecommerce.application.usecases.product.UpdateProductRatingUseCase;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Product;
import br.com.api.ecommerce.domain.models.Review;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteReviewUseCase {
    private final ReviewRepository repository;
    private final UpdateProductRatingUseCase updateProductRatingUseCase;

    public void execute(UUID id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.review.not.found"));

        Product product = review.getProduct();
        product.getReviews().remove(review);
        repository.delete(review);

        updateProductRatingUseCase.execute(product.getId());
    }
}
