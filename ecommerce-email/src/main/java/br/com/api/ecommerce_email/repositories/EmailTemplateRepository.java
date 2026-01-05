package br.com.api.ecommerce_email.repositories;

import br.com.api.ecommerce_email.models.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {
    Optional<EmailTemplate> findByEventType(String eventType);

    boolean existsByEventType(String eventType);
}
