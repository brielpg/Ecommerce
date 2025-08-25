package br.com.api.ecommerce.models.dtos.Cart;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartDtoItemRequest(
        @NotNull(message = "{dto.cart.id.notnull}")
        UUID productId,
        @NotNull(message = "{dto.cart.quantity.notnull}")
        Integer quantity
) {
}
