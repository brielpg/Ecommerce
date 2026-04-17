package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class SearchProductsUseCase {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final GetAllProductsUseCase getAllProductsUseCase;

    public Page<ProductDtoList> execute(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProductsUseCase.execute(pageable);
        }

        return repository.searchProducts(query, pageable)
                .map(mapper::toDto);
    }
}
