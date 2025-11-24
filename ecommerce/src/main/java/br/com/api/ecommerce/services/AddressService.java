package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Address;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.models.dtos.Address.AddressDtoList;
import br.com.api.ecommerce.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    public List<Address> create(List<AddressDtoCreate> dtos, User user){
        return dtos.stream()
                .map(dto -> {
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
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Address getById(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.address.not.found"));
    }

    @Transactional(readOnly = true)
    public List<Address> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public void existsByIdAndUser(UUID addressId, UUID userId){
        if (!repository.existsByIdAndUserId(addressId, userId))
            throw new NotFoundException("exception.address.not.found");
    }

    public AddressDtoList entityToDto(Address entity) {
        return new AddressDtoList(entity.getId(),
                entity.getStreetName(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode()
        );
    }
}
