package br.com.api.ecommerce.application.usecases.address;

import br.com.api.ecommerce.application.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.application.mappers.AddressMapper;
import br.com.api.ecommerce.domain.models.Address;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateAddressesUseCase {
    private final AddressMapper mapper;

    public List<Address> execute(List<AddressDtoCreate> dtos, User user) {
        return mapper.toEntityList(dtos, user);
    }
}
