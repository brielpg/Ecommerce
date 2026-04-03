package br.com.api.ecommerce.application.dtos.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserDtoList(
        UUID id,
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        Boolean active,
        LocalDateTime createdAt
) {
}
