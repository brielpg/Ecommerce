package br.com.api.ecommerce.infrastructure.implementations;

import br.com.api.ecommerce.domain.models.User;
import br.com.api.ecommerce.domain.enums.EventTypes;
import br.com.api.ecommerce.application.interfaces.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RabbitMqEmailImpl implements MessagePublisher {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${email.queue.name}")
    private String queueName;

    @Override
    public void publishUserEvent(EventTypes eventType, User user) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("eventType", eventType.toString());
            payload.put("data", Map.of(
                    "emailTo", user.getEmail(),
                    "userName", user.getName(),
                    "userPhone", user.getPhone()
            ));

            log.info("Publicando evento {} para a fila {}", eventType, queueName);
            rabbitTemplate.convertAndSend(queueName, payload);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para RabbitMQ. Tipo: {}", eventType, e);
        }
    }
}
