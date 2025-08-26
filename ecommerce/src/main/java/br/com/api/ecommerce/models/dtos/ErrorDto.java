package br.com.api.ecommerce.models.dtos;

import java.util.List;

public record ErrorDto(
        int status,
        String error,
        List<String> messages
) {
}
