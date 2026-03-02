package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses a WHERE u.id = :id AND u.active = true")
    Optional<User> findByIdAndActiveTrue(UUID id);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String email);

    UserDetails findByEmail(String email);

    @Query("SELECT p FROM User u JOIN u.favorites p WHERE u.id = :id")
    List<Product> getFavorites(UUID id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tb_user_favorites (user_id, product_id) VALUES (:userId, :productId)", nativeQuery = true)
    void addFavorite(UUID userId, UUID productId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tb_user_favorites WHERE user_id = :userId AND product_id = :productId", nativeQuery = true)
    void removeFavorite(UUID userId, UUID productId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tb_user_favorites WHERE product_id = :productId", nativeQuery = true)
    void removeProductFromAllFavorites(UUID productId);
}