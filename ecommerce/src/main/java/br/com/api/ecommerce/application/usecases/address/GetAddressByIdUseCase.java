package br.com.api.ecommerce.application.usecases.address;

import br.com.api.ecommerce.application.repositories.AddressRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Address;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetAddressByIdUseCase {
    private final AddressRepository repository;

    public Address execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.address.not.found"));
    }
}
