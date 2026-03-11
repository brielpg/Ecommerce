package br.com.api.ecommerce.core.mappers;

import br.com.api.ecommerce.core.models.Review;
import br.com.api.ecommerce.core.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.core.dtos.Review.ReviewDtoList;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewDtoCreate dto) {
        Review review = new Review();
        review.setRating(dto.rating());
        review.setReview(dto.review());

        return review;
    }

    public ReviewDtoList toDto(Review entity) {
        return new ReviewDtoList(
                entity.getId(),
                entity.getUser().getId(),
                entity.getProduct().getId(),
                entity.getRating(),
                entity.getReview(),
                entity.getCreatedAt()
        );
    }
}
