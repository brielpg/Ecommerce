package br.com.api.ecommerce.models.dtos.Category;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CategoryDtoForm(
        UUID id,
        @NotBlank(message = "{dto.category.name.notblank}")
        String name,
        @NotBlank(message = "{dto.category.description.notblank}")
        String description
) {
}
