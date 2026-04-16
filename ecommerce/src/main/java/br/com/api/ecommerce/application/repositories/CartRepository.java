package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Cart;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findCartByUserId(@Param("userId") UUID userId);
    void deleteAllItemsFromUserCart(@Param("userId") UUID userId);
    void resetCartTotal(@Param("userId") UUID userId);
    List<Cart> findCartsContainingProduct(@Param("productId") UUID productId);
}
