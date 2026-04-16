package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Product;
import br.com.api.ecommerce.domain.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByIdAndActiveTrue(UUID id);
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
    List<Product> getFavorites(UUID id);
    void addFavorite(UUID userId, UUID productId);
    void removeFavorite(UUID userId, UUID productId);
    void removeProductFromAllFavorites(UUID productId);
}
