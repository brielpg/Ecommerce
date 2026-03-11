package br.com.api.ecommerce.core.mappers;

import br.com.api.ecommerce.core.models.Address;
import br.com.api.ecommerce.core.models.User;
import br.com.api.ecommerce.core.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.core.dtos.Address.AddressDtoList;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AddressMapper {

    public AddressDtoList toDto(Address entity) {
        if (entity == null) return null;
        return new AddressDtoList(
                entity.getId(),
                entity.getStreetName(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode()
        );
    }

    public Address toEntity(AddressDtoCreate dto, User user) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreetName(dto.streetName());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
        address.setUser(user);

        return address;
    }

    public List<Address> toEntityList(List<AddressDtoCreate> dtos, User user) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(dto ->
                toEntity(dto, user))
                .toList();
    }
}
