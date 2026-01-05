package br.com.api.ecommerce.config;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.enums.UserRoles;
import br.com.api.ecommerce.repositories.UserRepository;
import br.com.api.ecommerce.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TemplateDataInitializer implements CommandLineRunner {

    @Value("${api.setup.create-initial-admin}")
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
                System.out.println("Admin user created");
            }
        }
    }
}