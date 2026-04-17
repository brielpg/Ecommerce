package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Optional<Review> findById(UUID id);
    Page<Review> findAllByProductId(Pageable pageable, UUID productId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
    Review save(Review review);
    void delete(Review review);
}
