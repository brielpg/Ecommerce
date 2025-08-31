package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.Order;
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
}
