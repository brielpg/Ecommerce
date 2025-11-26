package br.com.api.ecommerce.models.dtos.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProductDtoList(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Double rating,
        Boolean active,
        LocalDate timestamp
) {
}
