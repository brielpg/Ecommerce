package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
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
        this.existsByName(dto.name());

        Category category = dtoToEntity(dto);

        this.save(category);
        return category;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDtoList> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Category getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.category.not.found"));
    }

    @Transactional
    public Category update(CategoryDtoUpdate dto) {
        Category category = this.getById(dto.id());
        this.existsByName(dto.name());

        if (dto.name() != null) category.setName(dto.name());
        if (dto.description() != null) category.setDescription(dto.description());

        this.save(category);
        return category;
    }

    @Transactional
    public void delete(UUID id) {
        Category category = this.getById(id);

        category.setActive(false);
        this.save(category);
    }

    @Transactional(readOnly = true)
    private void existsByName(String name){
        if (repository.existsByName(name))
            throw new ConflictException("exception.category.name.already.exists");
    }

    @Transactional(readOnly = true)
    public List<Category> getCategoriesByIds(List<UUID> ids) {
        return repository.findAllByIdInAndActiveTrue(ids);
    }

    @Transactional
    public void save(Category category){
        repository.save(category);
    }

    private Category dtoToEntity(CategoryDtoCreate dto){
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        return category;
    }
}
