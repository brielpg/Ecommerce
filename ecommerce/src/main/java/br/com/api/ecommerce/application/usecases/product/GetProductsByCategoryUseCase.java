package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RequiredArgsConstructor
public class GetProductsByCategoryUseCase {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Page<ProductDtoList> execute(UUID categoryId, Pageable pageable) {
        return repository.findAllByCategoryId(categoryId, false, pageable)
                .map(mapper::toDto);
    }
}
