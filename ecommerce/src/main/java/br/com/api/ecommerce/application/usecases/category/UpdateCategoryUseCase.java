package br.com.api.ecommerce.application.usecases.category;

import br.com.api.ecommerce.application.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.application.mappers.CategoryMapper;
import br.com.api.ecommerce.application.repositories.CategoryRepository;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.Category;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateCategoryUseCase {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryDtoList execute(CategoryDtoUpdate dto) {
        Category category = getCategoryByIdUseCase.execute(dto.id());

        if (dto.name() != null && repository.existsByName(dto.name())) {
            throw new ConflictException("exception.category.name.already.exists");
        }

        if (dto.name() != null) category.setName(dto.name());
        if (dto.description() != null) category.setDescription(dto.description());

        repository.save(category);
        return mapper.toDto(category);
    }
}
