package br.com.api.ecommerce.models.dtos.Product;

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
        @NotBlank(message = "{dto.product.price.notblank}")
        BigDecimal price,
        @NotNull(message = "{dto.product.stock.notnull}")
        Integer stock,
        List<UUID> categories
) {
}