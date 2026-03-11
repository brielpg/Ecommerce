package br.com.api.ecommerce.infrastructure.security;

import br.com.api.ecommerce.core.models.User;
import br.com.api.ecommerce.core.models.enums.UserRoles;
import br.com.api.ecommerce.infrastructure.repositories.UserRepository;
import br.com.api.ecommerce.services.AuthorizationService;
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
    private AuthorizationService authorizationService;

    @Override
    public void run(String... args) throws Exception {
        if (shouldCreateAdmin) {
            if (!userRepository.existsByEmail("admin@email.com") && userRepository.count() == 0) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@email.com");
                admin.setPassword(authorizationService.encodePassword("123"));
                admin.setRole(UserRoles.ADMIN);
                admin.setActive(true);

                userRepository.save(admin);
                log.info("Setup inicial: Usuário ADMIN criado com sucesso");
            }
        }
    }
}