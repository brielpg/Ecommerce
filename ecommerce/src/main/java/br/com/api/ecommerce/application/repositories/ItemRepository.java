package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Item;

import java.util.List;
import java.util.UUID;

public interface ItemRepository {
    List<Item> findAllByProductId(UUID productId);
}
