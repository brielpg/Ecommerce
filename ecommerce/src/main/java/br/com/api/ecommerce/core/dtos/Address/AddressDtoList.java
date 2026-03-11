package br.com.api.ecommerce.core.dtos.Address;

import java.util.UUID;

public record AddressDtoList(
        UUID id,
        String streetName,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
