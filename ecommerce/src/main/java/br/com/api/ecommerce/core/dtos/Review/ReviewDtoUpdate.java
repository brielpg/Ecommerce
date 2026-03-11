package br.com.api.ecommerce.core.dtos.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewDtoUpdate(
        @NotNull(message = "{dto.review.id.notnull}")
        UUID id,
        @Min(value = 1, message = "{dto.review.rating.min}")
        @Max(value = 5, message = "{dto.review.rating.max}")
        Double rating,
        String review
) {
}
