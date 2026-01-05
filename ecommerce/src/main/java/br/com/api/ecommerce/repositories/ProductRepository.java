package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.name = ?1")
    boolean existsByName(String name);

    @Query("SELECT COUNT(*) > 0 FROM Product p JOIN p.categories c WHERE p.id = :productId AND c.id = :categoryId")
    boolean existsCategoryInProduct(@Param("productId") UUID productId, @Param("categoryId") UUID categoryId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.categories c WHERE p.id = :id AND p.active = true")
    Optional<Product> findByIdAndActiveTrue(UUID id);

    Page<Product> findAllByActiveTrue(Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO tb_product_category (product_id, category_id) VALUES (:productId, :categoryId)", nativeQuery = true)
    void addCategoryToProduct(@Param("productId") UUID productId, @Param("categoryId") UUID categoryId);

    @Modifying
    @Query(value = "DELETE FROM tb_product_category WHERE product_id = :productId AND category_id = :categoryId", nativeQuery = true)
    void removeCategoryFromProduct(@Param("productId") UUID productId, @Param("categoryId") UUID categoryId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Optional<Double> findAverageRatingByProductId(@Param("productId") UUID productId);

    @Query("SELECT p FROM Product p WHERE p.active = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
}
