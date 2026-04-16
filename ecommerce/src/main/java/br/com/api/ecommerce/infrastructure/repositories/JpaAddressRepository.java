package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.application.repositories.AddressRepository;
import br.com.api.ecommerce.domain.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaAddressRepository extends AddressRepository, JpaRepository<Address, UUID> {
}
