package br.com.api.ecommerce.models.dtos.Auth;

import jakarta.validation.constraints.NotBlank;

public record AuthDtoLogin(
        @NotBlank(message = "{dto.auth.login.notblank}")
        String login,
        @NotBlank(message = "{dto.auth.password.notblank}")
        String password
) {
}
