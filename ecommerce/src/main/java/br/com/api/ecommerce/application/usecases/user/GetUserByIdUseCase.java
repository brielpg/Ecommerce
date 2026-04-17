package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetUserByIdUseCase {
    private final UserRepository repository;

    public User execute(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.user.not.found"));
    }
}
