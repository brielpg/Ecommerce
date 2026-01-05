package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.enums.EventTypes;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${email.queue.name}")
    private String queueName;

    public void publishEvent(EventTypes eventType, Map<String, Object> data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", eventType.toString());
        payload.put("data", data);

        rabbitTemplate.convertAndSend(queueName, payload);
    }
}