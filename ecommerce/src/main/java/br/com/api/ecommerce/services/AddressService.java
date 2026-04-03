package br.com.api.ecommerce.services;

import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Address;
import br.com.api.ecommerce.domain.models.User;
import br.com.api.ecommerce.application.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.infrastructure.repositories.AddressRepository;
import br.com.api.ecommerce.application.mappers.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;
    private final AddressMapper mapper;

    public List<Address> create(List<AddressDtoCreate> dtos, User user) {
        return mapper.toEntityList(dtos, user);
    }

    @Transactional(readOnly = true)
    public Address getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.address.not.found"));
    }

    @Transactional(readOnly = true)
    public void existsByIdAndUser(UUID addressId, UUID userId) {
        if (!repository.existsByIdAndUserId(addressId, userId))
            throw new NotFoundException("exception.address.not.found");
    }
}
