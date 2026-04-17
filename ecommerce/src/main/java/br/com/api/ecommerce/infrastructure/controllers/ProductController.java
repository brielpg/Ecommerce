package br.com.api.ecommerce.infrastructure.controllers;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoAddCategory;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoUpdate;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.usecases.product.*;
import br.com.api.ecommerce.domain.models.Product;
import br.com.api.ecommerce.infrastructure.config.SecurityConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Endpoints for managing products")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
@RequiredArgsConstructor
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final GetProductsByCategoryUseCase getProductsByCategoryUseCase;
    private final SearchProductsUseCase searchProductsUseCase;
    private final GetBestSellersUseCase getBestSellersUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductImageUseCase getProductImageUseCase;
    private final RestoreProductUseCase restoreProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final AddCategoriesToProductUseCase addCategoriesToProductUseCase;
    private final RemoveCategoryFromProductUseCase removeCategoryFromProductUseCase;
    private final ProductMapper mapper;

    @Operation(summary = "Create a new product", description = "Creates a new product with optional image upload. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "409", description = "Product name already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDtoList> create(@RequestPart("product") @Valid ProductDtoCreate dto, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        ProductDtoList product = createProductUseCase.execute(dto, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products. Admins see all products, users see only active ones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDtoList>> getAll(Pageable pageable){
        Page<ProductDtoList> products = getAllProductsUseCase.execute(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID. Admins see all products, users see only active ones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found and returned"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDtoList> getById(@PathVariable UUID id){
        Product product = getProductByIdUseCase.execute(id);
        ProductDtoList productDtoList = mapper.toDto(product);
        return ResponseEntity.ok(productDtoList);
    }

    @Operation(summary = "Get all products by category ID", description = "Retrieves all product by a specific category by its ID. Admins see all products, users see only active ones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDtoList>> getAllByCategoryId(@PathVariable UUID categoryId, Pageable pageable){
        Page<ProductDtoList> products = getProductsByCategoryUseCase.execute(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Search products", description = "Searches for products by name or description. Returns paginated results.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDtoList>> search(@RequestParam(value = "q", required = false) String query, Pageable pageable) {
        Page<ProductDtoList> products = searchProductsUseCase.execute(query, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get best-selling products", description = "Retrieves a list of best-selling products ordered by purchase count.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Best-sellers retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/best-sellers")
    public ResponseEntity<List<ProductDtoList>> getBestSellers(@RequestParam(value = "limit", defaultValue = "10") int limit){
        List<ProductDtoList> products = getBestSellersUseCase.execute(limit);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update product", description = "Updates an existing product with optional image upload. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Product name already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDtoList> update(@RequestPart("product") @Valid ProductDtoUpdate dto, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        ProductDtoList product = updateProductUseCase.execute(dto, imageFile);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get product image", description = "Retrieves the image of a specific product by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found or no image available"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable UUID id) {
        byte[] image = getProductImageUseCase.execute(id);

        if (image == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).contentLength(image.length).body(image);
    }

    @Operation(summary = "Restore product", description = "Restores a previously soft-deleted product by setting it as active. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product restored successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable UUID id){
        restoreProductUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete product", description = "Soft-deletes a product by setting it as inactive. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        deleteProductUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Add category to product", description = "Add a specific category to a product. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category added successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Product or category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/categories")
    public ResponseEntity<Void> addCategoriesInProduct(@PathVariable UUID id, @RequestBody @Valid ProductDtoAddCategory dto){
        addCategoriesToProductUseCase.execute(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Remove category from product", description = "Removes a specific category from an existing product. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category removed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Product or category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromProduct(@PathVariable UUID id, @PathVariable UUID categoryId){
        removeCategoryFromProductUseCase.execute(id, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
