package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, UUID> {
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.name = :name")
    boolean existsByName(String name);

    @Query("SELECT COUNT(*) > 0 FROM Product p JOIN p.categories c WHERE p.id = :productId AND c.id = :categoryId")
    boolean existsCategoryInProduct(UUID productId, UUID categoryId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Optional<Double> findAverageRatingByProductId(UUID productId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.categories c WHERE p.id = :id AND p.active = true")
    Optional<Product> findByIdAndActiveTrue(UUID id);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId AND (:isAdmin = true OR (p.active = true AND c.active = true))")
    Page<Product> findAllByCategoryId(UUID categoryId, boolean isAdmin, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(String query, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO tb_product_category (product_id, category_id) VALUES (:productId, :categoryId)", nativeQuery = true)
    void addCategoryToProduct(UUID productId, UUID categoryId);

    @Modifying
    @Query(value = "DELETE FROM tb_product_category WHERE product_id = :productId AND category_id = :categoryId", nativeQuery = true)
    void removeCategoryFromProduct(UUID productId, UUID categoryId);
}
