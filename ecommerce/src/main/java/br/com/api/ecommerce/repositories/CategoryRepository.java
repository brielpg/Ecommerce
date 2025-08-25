package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);

    Optional<Category> findByIdAndActiveTrue(UUID id);

    Page<Category> findAllByActiveTrue(Pageable pageable);

    List<Category> findAllByIdInAndActiveTrue(List<UUID> ids);
}
