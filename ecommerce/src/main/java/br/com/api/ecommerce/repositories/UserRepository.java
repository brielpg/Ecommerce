package br.com.api.ecommerce.repositories;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses a WHERE u.id = :id AND u.active = true")
    Optional<User> findByIdAndActiveTrue(UUID id);

    @Query("SELECT new br.com.api.ecommerce.models.dtos.User.UserDtoList(u.id, u.name, u.email) FROM User u WHERE u.active = true")
    Page<UserDtoList> findAllByActiveTrue(Pageable pageable);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String email);
}