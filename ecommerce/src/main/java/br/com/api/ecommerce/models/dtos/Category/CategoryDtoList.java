package br.com.api.ecommerce.models.dtos.Category;

import java.time.LocalDate;
import java.util.UUID;

public record CategoryDtoList(
        UUID id,
        String name,
        String description,
        Boolean active,
        LocalDate timestamp
) {
}
