package br.com.api.ecommerce.application.usecases.category;

import br.com.api.ecommerce.application.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.application.mappers.CategoryMapper;
import br.com.api.ecommerce.application.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class GetAllCategoriesUseCase {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public Page<CategoryDtoList> execute(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable)
                .map(mapper::toDto);
    }
}
