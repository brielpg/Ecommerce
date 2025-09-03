package br.com.api.ecommerce.models.dtos.User;

import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record UserDtoCreate(
        @NotBlank(message = "{dto.user.name.notblank}")
        String name,
        @Email(message = "{dto.user.email.email}")
        @NotBlank(message = "{dto.user.email.notblank}")
        String email,
        @NotBlank(message = "{dto.auth.password.notblank}")
        String password,
        @Pattern(regexp = "\\d{10,15}", message = "{dto.user.phone.pattern}")
        @NotBlank(message = "{dto.user.phone.notblank}")
        String phone,
        @Past(message = "{dto.user.birthdate.past}")
        @NotNull(message = "{dto.user.birthdate.notnull}")
        LocalDate birthDate,
        @NotEmpty(message = "{dto.user.addresses.notempty}")
        @Valid
        List<AddressDtoCreate> addresses
) {
}