package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.application.usecases.cart.RemoveProductFromAllCartsUseCase;
import br.com.api.ecommerce.application.usecases.user.RemoveProductFromAllFavoritesUseCase;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class DeleteProductUseCase {
    private final ProductRepository repository;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final RemoveProductFromAllCartsUseCase removeProductFromAllCartsUseCase;
    private final RemoveProductFromAllFavoritesUseCase removeProductFromAllFavoritesUseCase;

    public void execute(UUID id) {
        Product product = getProductByIdUseCase.execute(id);

        removeProductFromAllCartsUseCase.execute(id);
        removeProductFromAllFavoritesUseCase.execute(id);

        product.setActive(false);
        repository.save(product);
        log.info("Produto ID: {} desativado com sucesso", id);
    }
}
