package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.AccessDeniedException;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }

    public void validateCurrentUser(UUID targetUserId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (User) auth.getPrincipal();
        UUID authenticatedUserId = principal.getId();
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (authenticatedUserId.equals(targetUserId) || isAdmin)
            return;

        throw new AccessDeniedException("dto.auth.access.denied");
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
