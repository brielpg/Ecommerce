package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = ?1")
    boolean existsByName(String name);

    Optional<Category> findByIdAndActiveTrue(UUID id);

    Page<Category> findAllByActiveTrue(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.id IN :ids AND c.active = true")
    List<Category> findAllByIdInAndActiveTrue(List<UUID> ids);

    @Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = :productId AND (:isAdmin = true OR (p.active = true AND c.active = true))")
    Page<Category> findAllByProductId(UUID productId, boolean isAdmin, Pageable pageable);
}
