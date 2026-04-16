package br.com.api.ecommerce.application.repositories;

import java.util.UUID;

public interface AddressRepository {
    boolean existsByIdAndUserId(UUID addressId, UUID userId);
}
