package br.com.api.ecommerce.application.repositories;

import br.com.api.ecommerce.domain.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewRepository {
    Page<Review> findAllByProductId(Pageable pageable, UUID productId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}
