package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    Page<Coupon> findAllByActiveTrue(Pageable pageable);

    Optional<Coupon> findByIdAndActiveTrue(UUID id);

    Optional<Coupon> findByCodeAndActiveTrue(String code);

    @Query("SELECT COUNT(c) > 0 FROM Coupon c WHERE c.code = ?1")
    boolean existsByCode(String code);
}
