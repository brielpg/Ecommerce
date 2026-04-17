package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.application.usecases.category.GetCategoryByIdUseCase;
import br.com.api.ecommerce.domain.models.Category;
import br.com.api.ecommerce.domain.models.Product;
import br.com.api.ecommerce.application.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoveCategoryFromProductUseCase {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public void execute(UUID productId, UUID categoryId) {
        Product product = getProductByIdUseCase.execute(productId);
        Category category = getCategoryByIdUseCase.execute(categoryId);

        productRepository.removeCategoryFromProduct(productId, categoryId);

        productRepository.save(product);
        categoryRepository.save(category);
    }
}
