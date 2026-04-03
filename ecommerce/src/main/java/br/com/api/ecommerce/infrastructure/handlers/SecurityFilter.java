package br.com.api.ecommerce.infrastructure.handlers;

import br.com.api.ecommerce.domain.models.User;
import br.com.api.ecommerce.infrastructure.repositories.UserRepository;
import br.com.api.ecommerce.application.interfaces.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null) traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("traceId", traceId);

        try {
            var token = this.recoverToken(request);
            if (token != null) {
                String login = tokenProvider.validateToken(token);
                User user = (User) userRepository.findByEmail(login);

                if (user != null) {
                    MDC.put("userId", user.getId().toString());
                    log.debug("Usuário {} autenticado com sucesso", login);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
