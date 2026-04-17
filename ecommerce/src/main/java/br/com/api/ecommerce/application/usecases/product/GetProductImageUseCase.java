package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetProductImageUseCase {
    private final GetProductByIdUseCase getProductByIdUseCase;

    public byte[] execute(UUID id) {
        Product product = getProductByIdUseCase.execute(id);
        return product.getImage();
    }
}
