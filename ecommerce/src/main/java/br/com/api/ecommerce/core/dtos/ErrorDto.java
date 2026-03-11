package br.com.api.ecommerce.core.dtos;

import java.util.List;

public record ErrorDto(
        int status,
        String error,
        List<String> messages
) {
}
