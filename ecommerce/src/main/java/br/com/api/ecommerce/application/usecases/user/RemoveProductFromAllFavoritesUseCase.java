package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoveProductFromAllFavoritesUseCase {
    private final UserRepository repository;

    public void execute(UUID productId) {
        repository.removeProductFromAllFavorites(productId);
    }
}
