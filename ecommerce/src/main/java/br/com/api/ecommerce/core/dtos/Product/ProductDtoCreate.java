package br.com.api.ecommerce.core.dtos.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductDtoCreate(
        @NotBlank(message = "{dto.product.name.notblank}")
        String name,
        @NotBlank(message = "{dto.product.description.notblank}")
        String description,
        @NotNull(message = "{dto.product.price.notnull}")
        @DecimalMin(value = "0.0", message = "{dto.product.price.min}")
        BigDecimal price,
        @NotNull(message = "{dto.product.stock.notnull}")
        @Min(value = 0, message = "{dto.product.stock.min}")
        Integer stock,
        List<UUID> categories
) {
}