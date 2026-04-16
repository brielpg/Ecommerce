package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.application.repositories.ItemRepository;
import br.com.api.ecommerce.domain.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaItemRepository extends ItemRepository, JpaRepository<Item, UUID> {
}
