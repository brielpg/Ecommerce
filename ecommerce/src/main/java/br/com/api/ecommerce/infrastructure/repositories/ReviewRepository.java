package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.core.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findAllByProductId(Pageable pageable, UUID productId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}
