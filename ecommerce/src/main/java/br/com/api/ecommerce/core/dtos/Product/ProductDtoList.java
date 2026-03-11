package br.com.api.ecommerce.core.dtos.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDtoList(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Double rating,
        Integer purchaseCount,
        Boolean active,
        LocalDateTime createdAt
) {
}
