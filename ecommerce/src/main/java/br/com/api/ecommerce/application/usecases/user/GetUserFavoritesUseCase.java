package br.com.api.ecommerce.application.usecases.user;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.repositories.UserRepository;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetUserFavoritesUseCase {
    private final UserRepository repository;
    private final ProductMapper productMapper;

    public List<ProductDtoList> execute(UUID userId) {
        List<Product> favorites = repository.getFavorites(userId);
        return favorites.stream().map(productMapper::toDto).toList();
    }
}
