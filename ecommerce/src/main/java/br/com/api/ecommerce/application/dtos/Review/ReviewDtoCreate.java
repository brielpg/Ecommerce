package br.com.api.ecommerce.application.dtos.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewDtoCreate(
        @NotNull(message = "{dto.review.userid.notnull}")
        UUID userId,
        @NotNull(message = "{dto.review.productid.notnull}")
        UUID productId,
        @NotNull(message = "{dto.review.rating.notnull}")
        @Min(value = 1, message = "{dto.review.rating.min}")
        @Max(value = 5, message = "{dto.review.rating.max}")
        Double rating,
        @NotBlank(message = "{dto.review.review.notblank}")
        String review
) {
}
