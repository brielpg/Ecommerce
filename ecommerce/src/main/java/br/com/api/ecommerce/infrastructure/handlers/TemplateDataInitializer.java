package br.com.api.ecommerce.infrastructure.handlers;

import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.domain.enums.UserRoles;
import br.com.api.ecommerce.domain.models.User;
import br.com.api.ecommerce.infrastructure.implementations.AuthorizationImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemplateDataInitializer implements CommandLineRunner {

    @Value("${api.setup.create-initial-admin:false}")
    private boolean shouldCreateAdmin;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationImpl authorizationImpl;

    @Override
    public void run(String... args) throws Exception {
        if (shouldCreateAdmin) {
            if (!userRepository.existsByEmail("admin@email.com") && userRepository.count() == 0) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@email.com");
                admin.setPassword(authorizationImpl.encode("123"));
                admin.setRole(UserRoles.ADMIN);
                admin.setActive(true);

                userRepository.save(admin);
                log.info("Setup inicial: Usuário ADMIN criado com sucesso");
            }
        }
    }
}