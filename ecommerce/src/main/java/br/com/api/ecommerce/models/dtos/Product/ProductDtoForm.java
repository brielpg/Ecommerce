package br.com.api.ecommerce.models.dtos.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductDtoForm(
        UUID id,
        String name,
        String description,
        @DecimalMin(value = "0.0", message = "{dto.product.price.min}")
        BigDecimal price,
        @Min(value = 0, message = "{dto.product.stock.min}")
        Integer stock,
        List<UUID> categories
) {
}
