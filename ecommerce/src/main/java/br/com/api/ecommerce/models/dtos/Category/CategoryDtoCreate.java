package br.com.api.ecommerce.models.dtos.Category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDtoCreate(
        @NotBlank(message = "{dto.category.name.notblank}")
        String name,
        @NotBlank(message = "{dto.category.description.notblank}")
        String descricao
) {
}
