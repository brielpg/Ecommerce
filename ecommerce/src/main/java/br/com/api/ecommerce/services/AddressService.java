package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.Address;
import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    public List<Address> create(List<AddressDtoCreate> dtos){
        List<Address> addresses = dtos.stream()
                .map(dto -> {
                    Address address = new Address();
                    address.setStreetName(dto.streetName());
                    address.setNumber(dto.number());
                    address.setComplement(dto.complement());
                    address.setNeighborhood(dto.neighborhood());
                    address.setCity(dto.city());
                    address.setState(dto.state());
                    address.setZipCode(dto.zipCode());
                    return address;
                })
                .toList();

        return repository.saveAll(addresses);
    }
}
