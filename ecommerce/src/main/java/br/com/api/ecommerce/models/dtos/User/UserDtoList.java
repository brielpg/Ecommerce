package br.com.api.ecommerce.models.dtos.User;

import java.time.LocalDate;
import java.util.UUID;

public record UserDtoList(
        UUID id,
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        Boolean active,
        LocalDate timestamp
) {
}
