package br.com.api.ecommerce.models.dtos.User;

import java.util.UUID;

public record UserDtoList(
        UUID id,
        String name,
        String email
) {
}
