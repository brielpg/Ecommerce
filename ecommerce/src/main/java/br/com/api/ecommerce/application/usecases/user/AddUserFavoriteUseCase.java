package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AddUserFavoriteUseCase {
    private final UserRepository repository;

    public void execute(UUID userId, UUID productId) {
        repository.addFavorite(userId, productId);
    }
}
