package br.com.api.ecommerce.application.usecases.address;

import br.com.api.ecommerce.application.repositories.AddressRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ValidateAddressOwnershipUseCase {
    private final AddressRepository repository;

    public void execute(UUID addressId, UUID userId) {
        if (!repository.existsByIdAndUserId(addressId, userId))
            throw new NotFoundException("exception.address.not.found");
    }
}
