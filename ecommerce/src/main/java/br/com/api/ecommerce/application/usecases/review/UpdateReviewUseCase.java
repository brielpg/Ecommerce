package br.com.api.ecommerce.application.usecases.review;

import br.com.api.ecommerce.application.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.application.dtos.Review.ReviewDtoUpdate;
import br.com.api.ecommerce.application.mappers.ReviewMapper;
import br.com.api.ecommerce.application.repositories.ReviewRepository;
import br.com.api.ecommerce.application.usecases.product.UpdateProductRatingUseCase;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Review;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateReviewUseCase {
    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    private final UpdateProductRatingUseCase updateProductRatingUseCase;

    public ReviewDtoList execute(ReviewDtoUpdate dto) {
        Review review = repository.findById(dto.id())
                .orElseThrow(() -> new NotFoundException("exception.review.not.found"));

        if (dto.rating() != null) review.setRating(dto.rating());
        if (dto.review() != null) review.setReview(dto.review());

        repository.save(review);

        updateProductRatingUseCase.execute(review.getProduct().getId());

        return mapper.toDto(review);
    }
}
