package br.com.api.ecommerce.application.usecases.category;

import br.com.api.ecommerce.application.repositories.CategoryRepository;
import br.com.api.ecommerce.domain.models.Category;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RestoreCategoryUseCase {
    private final CategoryRepository repository;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public void execute(UUID id) {
        Category category = getCategoryByIdUseCase.execute(id);
        category.setActive(true);
        repository.save(category);
    }
}
