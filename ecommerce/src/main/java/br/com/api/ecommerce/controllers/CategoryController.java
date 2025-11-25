package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDtoList> create(@RequestBody @Valid CategoryDtoCreate dto){
        CategoryDtoList category = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDtoList>> getAll(Pageable pageable){
        Page<CategoryDtoList> categories = service.getAll(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDtoList> getById(@PathVariable UUID id){
        Category category = service.getById(id);
        CategoryDtoList categoryDtoList = service.entityToDto(category);
        return ResponseEntity.ok(categoryDtoList);
    }

    @PutMapping
    public ResponseEntity<CategoryDtoList> update(@RequestBody @Valid CategoryDtoUpdate dto){
        CategoryDtoList category = service.update(dto);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable UUID id){
        service.restore(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
