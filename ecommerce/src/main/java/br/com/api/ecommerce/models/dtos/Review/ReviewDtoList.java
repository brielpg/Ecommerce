package br.com.api.ecommerce.models.dtos.Review;

import java.time.LocalDate;
import java.util.UUID;

public record ReviewDtoList(
        UUID id,
        UUID userId,
        UUID productId,
        Double rating,
        String review,
        LocalDate timestamp
) {
}
