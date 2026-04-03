package br.com.api.ecommerce.application.dtos.Category;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CategoryDtoUpdate(
        @NotNull(message = "{dto.category.id.notnull}")
        UUID id,
        String name,
        String description
) {
}
