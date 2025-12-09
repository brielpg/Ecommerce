package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "Endpoints for managing categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @Operation(summary = "Create category", description = "Create a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successful"),
        @ApiResponse(responseCode = "409", description = "Category name already exists"),
        @ApiResponse(responseCode = "500", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CategoryDtoList> create(@RequestBody @Valid CategoryDtoCreate dto){
        CategoryDtoList category = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a paginated list of all categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CategoryDtoList>> getAll(Pageable pageable){
        Page<CategoryDtoList> categories = service.getAll(pageable);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by Id", description = "Retrieves the category by Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDtoList> getById(@PathVariable UUID id){
        Category category = service.getById(id);
        CategoryDtoList categoryDtoList = service.entityToDto(category);
        return ResponseEntity.ok(categoryDtoList);
    }

    @Operation(summary = "Update category", description = "Updates an existing category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category name already exists"),
        @ApiResponse(responseCode = "500", description = "Invalid input data")
    })
    @PutMapping
    public ResponseEntity<CategoryDtoList> update(@RequestBody @Valid CategoryDtoUpdate dto){
        CategoryDtoList category = service.update(dto);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Restore category", description = "Restores a soft-deleted category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category restored successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable UUID id){
        service.restore(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete category", description = "Soft-delete category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
