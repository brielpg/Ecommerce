package br.com.api.ecommerce_email.models.dtos;

import java.util.Map;

public record EmailDto(
        String emailTo,
        String subject,
        String templateName,
        Map<String, Object> variables
) {
}
