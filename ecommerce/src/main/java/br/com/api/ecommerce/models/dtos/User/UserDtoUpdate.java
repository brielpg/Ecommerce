package br.com.api.ecommerce.models.dtos.User;

import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UserDtoUpdate(
        @NotNull(message = "{dto.user.id.notnull}")
        UUID id,
        String name,
        @Email(message = "{dto.user.email.email}")
        String email,
        @Pattern(regexp = "\\d{10,15}", message = "{dto.user.phone.pattern}")
        String phone,
        @Past(message = "{dto.user.birthdate.past}")
        LocalDate birthDate
) {
}