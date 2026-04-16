package br.com.api.ecommerce.infrastructure.repositories;

import br.com.api.ecommerce.application.repositories.ReviewRepository;
import br.com.api.ecommerce.domain.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, UUID> {
}
