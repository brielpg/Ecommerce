package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    Optional<Address> findById(UUID id);
    boolean existsByIdAndUserId(UUID addressId, UUID userId);
}
