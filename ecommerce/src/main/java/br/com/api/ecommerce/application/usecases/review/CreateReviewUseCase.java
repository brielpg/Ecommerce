package br.com.api.ecommerce.application.usecases.review;

import br.com.api.ecommerce.application.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.application.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.application.mappers.ReviewMapper;
import br.com.api.ecommerce.application.repositories.ReviewRepository;
import br.com.api.ecommerce.application.usecases.order.CheckOrderExistsByStatusUseCase;
import br.com.api.ecommerce.application.usecases.product.GetProductByIdUseCase;
import br.com.api.ecommerce.application.usecases.product.UpdateProductRatingUseCase;
import br.com.api.ecommerce.application.usecases.user.GetUserByIdUseCase;
import br.com.api.ecommerce.domain.enums.OrderStatus;
import br.com.api.ecommerce.domain.exceptions.AccessDeniedException;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.Review;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateReviewUseCase {
    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final CheckOrderExistsByStatusUseCase checkOrderExistsByStatusUseCase;
    private final UpdateProductRatingUseCase updateProductRatingUseCase;

    public ReviewDtoList execute(ReviewDtoCreate dto) {
        if (repository.existsByUserIdAndProductId(dto.userId(), dto.productId())) {
            throw new ConflictException("exception.review.already.exists");
        }

        if (!checkOrderExistsByStatusUseCase.execute(dto.userId(), dto.productId(), OrderStatus.DELIVERED)) {
            throw new AccessDeniedException("exception.review.purchase.required");
        }

        Review review = mapper.toEntity(dto);
        review.setUser(getUserByIdUseCase.execute(dto.userId()));
        review.setProduct(getProductByIdUseCase.execute(dto.productId()));

        repository.save(review);

        updateProductRatingUseCase.execute(dto.productId());

        return mapper.toDto(review);
    }
}
