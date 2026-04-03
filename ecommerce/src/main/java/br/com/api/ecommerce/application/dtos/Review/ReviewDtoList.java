package br.com.api.ecommerce.application.dtos.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDtoList(
        UUID id,
        UUID userId,
        UUID productId,
        Double rating,
        String review,
        LocalDateTime createdAt
) {
}
