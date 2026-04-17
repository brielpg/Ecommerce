package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoAddCategory;
import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.application.usecases.category.GetCategoriesByIdsUseCase;
import br.com.api.ecommerce.domain.models.Category;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AddCategoriesToProductUseCase {
    private final ProductRepository repository;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final GetCategoriesByIdsUseCase getCategoriesByIdsUseCase;

    public void execute(UUID id, ProductDtoAddCategory dto) {
        Product product = getProductByIdUseCase.execute(id);
        List<Category> categories = getCategoriesByIdsUseCase.execute(dto.categories());

        for (Category category : categories)
            if (!repository.existsCategoryInProduct(id, category.getId()))
                repository.addCategoryToProduct(id, category.getId());

        repository.save(product);
    }
}
