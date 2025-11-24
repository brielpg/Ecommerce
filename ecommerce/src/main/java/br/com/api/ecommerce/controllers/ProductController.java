package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoAddCategory;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoUpdate;
import br.com.api.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ResponseEntity<ProductDtoList> create(@RequestBody @Valid ProductDtoCreate dto){
        ProductDtoList product = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDtoList>> getAll(Pageable pageable){
        Page<ProductDtoList> products = service.getAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDtoList> getById(@PathVariable UUID id){
        Product product = service.getById(id);
        ProductDtoList productDtoList = service.entityToDto(product);
        return ResponseEntity.ok(productDtoList);
    }

    @PutMapping
    public ResponseEntity<ProductDtoList> update(@RequestBody @Valid ProductDtoUpdate dto){
        ProductDtoList product = service.update(dto);
        return ResponseEntity.ok(product);
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

    @PostMapping("/{id}/categories")
    public ResponseEntity<Void> addCategoriesInProduct(@PathVariable UUID id, @RequestBody @Valid ProductDtoAddCategory dto){
        service.addCategoriesInProduct(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromProduct(@PathVariable UUID id, @PathVariable UUID categoryId){
        service.removeCategoryFromProduct(id, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
