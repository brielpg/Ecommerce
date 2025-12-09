package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o JOIN FETCH o.user u WHERE u.id = :userId AND u.active = true")
    Page<Order> findOrdersByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.items i WHERE o.user.id = :userId AND i.product.id = :productId AND o.status = :status")
    boolean existsByUserIdAndProductIdAndStatus(@Param("userId") UUID userId, @Param("productId") UUID productId, @Param("status") OrderStatus status);
}
