package br.com.api.ecommerce.application.usecases.category;

import br.com.api.ecommerce.application.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.application.mappers.CategoryMapper;
import br.com.api.ecommerce.application.repositories.CategoryRepository;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.Category;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCategoryUseCase {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryDtoList execute(CategoryDtoCreate dto) {
        if (repository.existsByName(dto.name())) {
            throw new ConflictException("exception.category.name.already.exists");
        }

        Category category = mapper.toEntity(dto);
        repository.save(category);

        return mapper.toDto(category);
    }
}
