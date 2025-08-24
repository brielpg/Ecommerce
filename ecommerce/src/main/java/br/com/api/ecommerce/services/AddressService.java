package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.Address;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
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
}
