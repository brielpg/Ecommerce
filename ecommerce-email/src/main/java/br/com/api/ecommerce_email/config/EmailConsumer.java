package br.com.api.ecommerce_email.config;

import br.com.api.ecommerce_email.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class EmailConsumer {

    @Autowired
    private EmailService service;

    @RabbitListener(queues = "${email.queue.name}")
    public void listen(Map<String, Object> message, @Header(name = "traceId", required = false) String traceId, @Header(name = "userId", required = false) String userId) {
        try {
            String eventType = (String) message.get("eventType");
            Map<String, Object> data = (Map<String, Object>) message.get("data");

            MDC.put("traceId", traceId != null ? traceId : UUID.randomUUID().toString().substring(0,8));
            MDC.put("userId", userId);
            MDC.put("eventType", eventType);

            service.sendEmail(eventType, data);
        } finally {
            MDC.clear();
        }
    }
}