package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoveUserFavoriteUseCase {
    private final UserRepository repository;

    public void execute(UUID userId, UUID productId) {
        repository.removeFavorite(userId, productId);
    }
}
