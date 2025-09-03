package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoAddCategory;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoUpdate;
import br.com.api.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryService categoryService;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(ProductDtoCreate dto){
        this.existsByName(dto.name());

        Product product = dtoToEntity(dto);

        this.save(product);
        return product;
    }

    @Transactional(readOnly = true)
    public Page<ProductDtoList> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Product getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.product.not.found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(ProductDtoUpdate dto) {
        Product product = this.getById(dto.id());
        this.existsByName(dto.name());

        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.price() != null) product.setPrice(dto.price());
        if (dto.stock() != null) product.setStock(dto.stock());

        this.save(product);
        return product;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        Product product = this.getById(id);

        product.setActive(false);
        this.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addCategoriesInProduct(UUID id, ProductDtoAddCategory dto) {
        Product product = this.getById(id);
        List<Category> categories = categoryService.getCategoriesByIds(dto.categories());

        for (Category category : categories){
            if (!repository.existsCategoryInProduct(id, category.getId())) {
                repository.addCategoryToProduct(id, category.getId());
            }
        }

        this.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void removeCategoryFromProduct(UUID id, UUID categoryId) {
        Product product = this.getById(id);
        Category category = categoryService.getById(categoryId);

        repository.removeCategoryFromProduct(id, categoryId);

        this.save(product);
        categoryService.save(category);
    }

    @Transactional(readOnly = true)
    private void existsByName(String name){
        if (repository.existsByName(name))
            throw new ConflictException("exception.product.name.already.exists");
    }

    @Transactional
    public void save(Product product){
        repository.save(product);
    }

    private Product dtoToEntity(ProductDtoCreate dto){
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        if (!dto.categories().isEmpty())
            product.setCategories(categoryService.getCategoriesByIds(dto.categories()));

        return product;
    }
}
