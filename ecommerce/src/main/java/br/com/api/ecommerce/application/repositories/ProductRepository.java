package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Page<Product> findAllByActiveTrue(Pageable pageable);
    boolean existsByName(String name);
    boolean existsCategoryInProduct(UUID productId, UUID categoryId);
    Optional<Double> findAverageRatingByProductId(UUID productId);
    Optional<Product> findByIdAndActiveTrue(UUID id);
    Page<Product> findAllByCategoryId(UUID categoryId, boolean isAdmin, Pageable pageable);
    Page<Product> searchProducts(String query, Pageable pageable);
    void addCategoryToProduct(UUID productId, UUID categoryId);
    void removeCategoryFromProduct(UUID productId, UUID categoryId);
}
