package br.com.api.ecommerce_email.config;

import br.com.api.ecommerce_email.models.EmailTemplate;
import br.com.api.ecommerce_email.repositories.EmailTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TemplateDataInitializer {

    @Bean
    CommandLineRunner initTemplates(EmailTemplateRepository repository) {
        return args -> {
            List<String> defaultEvents = List.of("USER_WELCOME");

            for (String event : defaultEvents) {
                if (!repository.existsByEventType(event)) {
                    EmailTemplate template = new EmailTemplate();
                    template.setEventType(event);

                    repository.save(template);
                    System.out.println(event + " template created");
                }
            }
        };
    }
}