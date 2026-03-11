package br.com.api.ecommerce.core.dtos.Product;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ProductDtoAddCategory(
        @NotEmpty(message = "{dto.product.categories.notempty}")
        List<UUID> categories
) {
}
