package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class GetAllProductsUseCase {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Page<ProductDtoList> execute(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable)
                .map(mapper::toDto);
    }
}
