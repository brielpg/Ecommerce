package br.com.api.ecommerce.application.usecases.category;

import br.com.api.ecommerce.application.repositories.CategoryRepository;
import br.com.api.ecommerce.domain.exceptions.NotFoundException;
import br.com.api.ecommerce.domain.models.Category;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetCategoryByIdUseCase {
    private final CategoryRepository repository;

    public Category execute(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.category.not.found"));
    }
}
