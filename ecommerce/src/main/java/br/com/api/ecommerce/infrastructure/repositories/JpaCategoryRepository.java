package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.application.repositories.CategoryRepository;
import br.com.api.ecommerce.domain.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, UUID> {
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = ?1")
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE c.id IN :ids AND c.active = true")
    List<Category> findAllByIdInAndActiveTrue(List<UUID> ids);
}
