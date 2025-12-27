package br.com.api.ecommerce_email.repositories;

import br.com.api.ecommerce_email.models.EmailLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends MongoRepository<EmailLog, String> {
}
