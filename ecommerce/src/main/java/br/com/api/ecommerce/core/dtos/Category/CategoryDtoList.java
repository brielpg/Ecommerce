package br.com.api.ecommerce.core.dtos.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDtoList(
        UUID id,
        String name,
        String description,
        Boolean active,
        LocalDateTime createdAt
) {
}
