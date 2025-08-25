package br.com.api.ecommerce.models.dtos.Item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record DtoItemRequest(
        @NotNull(message = "{dto.item.id.notnull}")
        UUID productId,
        @NotNull(message = "{dto.item.quantity.notnull}")
        @Positive(message = "{dto.item.quantity.positive}")
        Integer quantity
) {
}
