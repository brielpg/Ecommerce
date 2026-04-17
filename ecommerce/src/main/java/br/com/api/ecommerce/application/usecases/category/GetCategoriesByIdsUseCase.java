package br.com.api.ecommerce.application.usecases.category;

import br.com.api.ecommerce.application.repositories.CategoryRepository;
import br.com.api.ecommerce.domain.models.Category;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetCategoriesByIdsUseCase {
    private final CategoryRepository repository;

    public List<Category> execute(List<UUID> ids) {
        return repository.findAllByIdInAndActiveTrue(ids);
    }
}
