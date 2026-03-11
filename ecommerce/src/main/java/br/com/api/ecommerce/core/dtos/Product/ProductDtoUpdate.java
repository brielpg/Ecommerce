package br.com.api.ecommerce.core.dtos.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDtoUpdate(
        @NotNull(message = "{dto.product.id.notnull}")
        UUID id,
        String name,
        String description,
        @DecimalMin(value = "0.0", message = "{dto.product.price.min}")
        BigDecimal price,
        @Min(value = 0, message = "{dto.product.stock.min}")
        Integer stock
) {
}