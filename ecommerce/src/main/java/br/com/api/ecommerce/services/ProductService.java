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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private CartService cartService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private ItemService itemService;

    @Autowired
    private AuthorizationService authorizationService;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDtoList create(ProductDtoCreate dto, MultipartFile imageFile){
        this.existsByName(dto.name());

        Product product = dtoToEntity(dto);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                product.setImage(imageFile.getBytes());
            } catch (IOException e) {
                log.error("Erro ao processar imagem do produto {}", dto.name(), e);
            }
        }

        this.save(product);
        log.info("Produto criado com sucesso, ID: {}", product.getId());
        return entityToDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDtoList> getAll(Pageable pageable) {
        Page<Product> products;

        if (authorizationService.validateAdminUser()) {
            products = repository.findAll(pageable);
        } else {
            products = repository.findAllByActiveTrue(pageable);
        }

        return products.map(this::entityToDto);
    }

    @Transactional(readOnly = true)
    public Product getById(UUID id) {
        if (authorizationService.validateAdminUser()) {
            Optional<Product> product = repository.findById(id);
            if (product.isPresent()) return product.get();
        }

        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.product.not.found"));
    }

    @Transactional(readOnly = true)
    public Page<ProductDtoList> getAllByCategoryId(UUID categoryId, Pageable pageable) {
        Page<Product> products;

        if (authorizationService.validateAdminUser()) {
            products = repository.findAllByCategoryId(categoryId, true, pageable);
        } else {
            products = repository.findAllByCategoryId(categoryId, false, pageable);
        }

        return products.map(this::entityToDto);
    }

    @Transactional(readOnly = true)
    @Cacheable("best-sellers")
    public List<ProductDtoList> getBestSellers(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "purchaseCount"));

        Page<ProductDtoList> products = this.getAll(pageable);

        return products.getContent();
    }

    @Transactional(readOnly = true)
    public Page<ProductDtoList> search(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return this.getAll(pageable); 
        }

        Page<Product> products = repository.searchProducts(query, pageable);

        return products.map(this::entityToDto);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "best-sellers", allEntries = true)
    public ProductDtoList update(ProductDtoUpdate dto, MultipartFile imageFile) {
        Product product = this.getById(dto.id());
        this.existsByName(dto.name());

        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.stock() != null) product.setStock(dto.stock());

        if (dto.price() != null && !dto.price().equals(product.getPrice())) {
            product.setPrice(dto.price());
            itemService.updateItemsPrice(product);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                product.setImage(imageFile.getBytes());
            } catch (IOException ignored) {}
        }

        this.save(product);
        return entityToDto(product);
    }

    @Transactional(readOnly = true)
    public byte[] getImageById(UUID id) {
        Product product = this.getById(id);

        return product.getImage();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void restore(UUID id) {
        Product product = this.getById(id);

        product.setActive(true);
        this.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        Product product = this.getById(id);

        cartService.removeProductFromAllCarts(id);
        userService.removeProductFromAllFavorites(id);

        product.setActive(false);
        this.save(product);
        log.info("Produto ID: {} desativado com sucesso", id);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addCategoriesInProduct(UUID id, ProductDtoAddCategory dto) {
        Product product = this.getById(id);
        List<Category> categories = categoryService.getCategoriesByIds(dto.categories());

        for (Category category : categories)
            if (!repository.existsCategoryInProduct(id, category.getId()))
                repository.addCategoryToProduct(id, category.getId());

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
    public void updateProductRating(UUID productId) {
        Product product = getById(productId);
        Optional<Double> averageRating = repository.findAverageRatingByProductId(productId);

        if (averageRating.isEmpty()) {
            product.setRating(0.0);
        } else {
            product.setRating(averageRating.get());
        }

        this.save(product);
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

    public ProductDtoList entityToDto(Product entity){
        return new ProductDtoList(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getRating(),
                entity.getPurchaseCount(),
                entity.getActive(),
                entity.getTimestamp()
        );
    }
}
