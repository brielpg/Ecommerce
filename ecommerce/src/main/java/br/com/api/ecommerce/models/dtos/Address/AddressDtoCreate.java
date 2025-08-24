package br.com.api.ecommerce.models.dtos.Address;

import jakarta.validation.constraints.NotBlank;

public record AddressDtoCreate(
        @NotBlank(message = "{dto.address.streetname.notblank}")
        String streetName,
        @NotBlank(message = "{dto.address.number.notblank}")
        String number,
        String complement,
        @NotBlank(message = "{dto.address.neighborhood.notblank}")
        String neighborhood,
        @NotBlank(message = "{dto.address.city.notblank}")
        String city,
        @NotBlank(message = "{dto.address.state.notblank}")
        String state,
        @NotBlank(message = "{dto.address.zipcode.notblank}")
        String zipCode
) {
}