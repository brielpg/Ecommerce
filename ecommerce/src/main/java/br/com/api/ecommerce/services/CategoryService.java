package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    @Transactional
    public Category create(CategoryDtoCreate dto){
        if (repository.existsByName(dto.name())) throw new RuntimeException();

        Category category = dtoToEntity(dto);

        this.save(category);
        return category;
    }

    @Transactional
    public void delete(UUID id) {
        Category category = this.getById(id);

        category.setActive(false);
        this.save(category);
    }

    @Transactional
    public Category update(CategoryDtoUpdate dto) {
        Category category = this.getById(dto.id());


        if (dto.name() != null) category.setName(dto.name());
        if (dto.description() != null) category.setDescription(dto.description());

        this.save(category);
        return category;
    }

    @Transactional(readOnly = true)
    public Category getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Page<Category> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    private Category dtoToEntity(CategoryDtoCreate dto){
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> getCategoriesByIds(List<UUID> ids) {
        return repository.findAllByIdInAndActiveTrue(ids);
    }

    @Transactional
    public void save(Category category){
        repository.save(category);
    }
}
