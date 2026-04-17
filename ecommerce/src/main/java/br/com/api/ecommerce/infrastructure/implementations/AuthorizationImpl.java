package br.com.api.ecommerce.infrastructure.implementations;

import br.com.api.ecommerce.application.dtos.Auth.AuthDtoLogin;
import br.com.api.ecommerce.application.dtos.Auth.AuthReturnToken;
import br.com.api.ecommerce.application.interfaces.AuthorizationProvider;
import br.com.api.ecommerce.application.interfaces.TokenProvider;
import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.domain.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorizationImpl implements UserDetailsService, AuthorizationProvider {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }

    @Override
    public boolean validateAdminUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

    @Override
    public String encode(String password){
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String currentPassword, String userPassword) {
        return passwordEncoder.matches(currentPassword, userPassword);
    }

    public AuthReturnToken login(AuthDtoLogin dto) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword =
                    new UsernamePasswordAuthenticationToken(dto.login(), dto.password());

            Authentication auth = this.authenticationManager.authenticate(usernamePassword);

            String token = tokenProvider.generateToken((User) auth.getPrincipal());

            log.info("Login bem-sucedido para o usuário: {}", dto.login());
            return new AuthReturnToken(token);
        } catch (AuthenticationException e) {
            log.warn("Falha de autenticação para o usuário: {}", dto.login(), e);
            throw e;
        }
    }
}
