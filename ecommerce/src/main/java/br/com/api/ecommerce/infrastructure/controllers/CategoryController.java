package br.com.api.ecommerce.infrastructure.controllers;

import br.com.api.ecommerce.infrastructure.config.SecurityConfiguration;
import br.com.api.ecommerce.domain.models.Category;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.services.CategoryService;
import br.com.api.ecommerce.application.mappers.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class CategoryController {

    @Autowired
    private CategoryService service;

    @Autowired
    private CategoryMapper mapper;

    @Operation(summary = "Create category", description = "Creates a new category. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "409", description = "Category name already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CategoryDtoList> create(@RequestBody @Valid CategoryDtoCreate dto){
        CategoryDtoList category = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a paginated list of all categories. Admin users see all categories while regular users only see active ones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CategoryDtoList>> getAll(Pageable pageable){
        Page<CategoryDtoList> categories = service.getAll(pageable);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a category by ID. Admin users can access any category while regular users can only access active ones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDtoList> getById(@PathVariable UUID id){
        Category category = service.getById(id);
        CategoryDtoList categoryDtoList = mapper.toDto(category);
        return ResponseEntity.ok(categoryDtoList);
    }

    @Operation(summary = "Update category", description = "Updates an existing category. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category name already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<CategoryDtoList> update(@RequestBody @Valid CategoryDtoUpdate dto){
        CategoryDtoList category = service.update(dto);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Restore category", description = "Restores a soft-deleted category. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category restored successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable UUID id){
        service.restore(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete category", description = "Soft-deletes a category. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
