package br.com.api.ecommerce.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        rabbitTemplate.addBeforePublishPostProcessors(message -> {
            String traceId = MDC.get("traceId");
            String userId = MDC.get("userId");

            log.debug("Injetando contexto de rastreio na mensagem RabbitMQ: traceId={}, userId={}", traceId, userId);

            if (traceId != null) {
                message.getMessageProperties().setHeader("traceId", traceId);
            }
            if (userId != null) {
                message.getMessageProperties().setHeader("userId", userId);
            }

            return message;
        });

        return rabbitTemplate;
    }
}
