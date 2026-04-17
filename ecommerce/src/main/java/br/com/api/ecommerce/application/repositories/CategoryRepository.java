package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<Category> findById(UUID id);
    boolean existsByName(String name);
    Optional<Category> findByIdAndActiveTrue(UUID id);
    Page<Category> findAll(Pageable pageable);
    Page<Category> findAllByActiveTrue(Pageable pageable);
    List<Category> findAllByIdInAndActiveTrue(List<UUID> ids);
    Category save(Category category);
}
