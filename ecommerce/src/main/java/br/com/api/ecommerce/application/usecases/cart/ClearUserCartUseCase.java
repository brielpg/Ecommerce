package br.com.api.ecommerce.application.usecases.cart;

import br.com.api.ecommerce.application.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class ClearUserCartUseCase {
    private final CartRepository repository;

    public void execute(UUID userId) {
        log.info("Limpando carrinho do usuário");
        repository.deleteAllItemsFromUserCart(userId);
        repository.resetCartTotal(userId);
    }
}
