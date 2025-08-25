package br.com.api.ecommerce.models.dtos.Product;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDtoUpdate(
        @NotNull(message = "{dto.product.id.notnull}")
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock
) {
}