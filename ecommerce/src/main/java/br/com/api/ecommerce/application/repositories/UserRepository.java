package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Product;
import br.com.api.ecommerce.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByIdAndActiveTrue(UUID id);
    Page<User> findAll(Pageable pageable);
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
    List<Product> getFavorites(UUID id);
    void addFavorite(UUID userId, UUID productId);
    void removeFavorite(UUID userId, UUID productId);
    void removeProductFromAllFavorites(UUID productId);
    User save(User user);
}
