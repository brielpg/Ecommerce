package br.com.api.ecommerce_email.config;

import br.com.api.ecommerce_email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailConsumer {

    @Autowired
    private EmailService service;

    @RabbitListener(queues = "${email.queue.name}")
    public void listen(Map<String, Object> message) {
        String eventType = (String) message.get("eventType");
        Map<String, Object> data = (Map<String, Object>) message.get("data");
        service.sendEmail(eventType, data);
    }
}