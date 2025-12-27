package br.com.api.ecommerce_email.config;

import br.com.api.ecommerce_email.models.dtos.EmailDto;
import br.com.api.ecommerce_email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    private EmailService service;

    @RabbitListener(queues = "${email.queue.name}")
    public void listen(@Payload EmailDto dto) {
        service.sendEmail(dto);
    }
}