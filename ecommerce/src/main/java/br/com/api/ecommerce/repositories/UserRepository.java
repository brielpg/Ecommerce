package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByIdAndActiveTrue(UUID id);
    Page<User> findAllByActiveTrue(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByIdAndActiveTrue(UUID userId);
}
