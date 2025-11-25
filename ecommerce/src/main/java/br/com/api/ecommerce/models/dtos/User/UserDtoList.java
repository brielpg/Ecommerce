package br.com.api.ecommerce.models.dtos.User;

import br.com.api.ecommerce.models.dtos.Address.AddressDtoList;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UserDtoList(
        UUID id,
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        List<AddressDtoList> addresses,
        Boolean active,
        LocalDate timestamp
) {
}
