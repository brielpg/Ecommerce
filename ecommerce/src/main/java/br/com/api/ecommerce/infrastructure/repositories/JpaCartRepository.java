package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.application.repositories.CartRepository;
import br.com.api.ecommerce.domain.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCartRepository extends CartRepository, JpaRepository<Cart, UUID> {
    @Query("SELECT DISTINCT c FROM Cart c JOIN FETCH c.user u LEFT JOIN FETCH c.items i WHERE u.id = :userId AND u.active = true")
    Optional<Cart> findCartByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM Item i WHERE i.cart.user.id = :userId")
    void deleteAllItemsFromUserCart(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Cart c SET c.totalPrice = 0 WHERE c.user.id = :userId")
    void resetCartTotal(@Param("userId") UUID userId);

    @Query("SELECT DISTINCT c FROM Cart c JOIN c.items i WHERE i.product.id = :productId")
    List<Cart> findCartsContainingProduct(@Param("productId") UUID productId);
}
