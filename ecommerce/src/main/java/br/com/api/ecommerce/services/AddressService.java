package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Address;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.repositories.AddressRepository;
import br.com.api.ecommerce.services.mappers.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private AddressMapper mapper;

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
