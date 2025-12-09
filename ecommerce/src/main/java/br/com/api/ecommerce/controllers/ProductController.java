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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDtoList> create(@RequestPart("product") @Valid ProductDtoCreate dto, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        ProductDtoList product = service.create(dto, imageFile);
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

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDtoList>> search(@RequestParam(value = "q", required = false) String query, Pageable pageable) {
        Page<ProductDtoList> products = service.search(query, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<List<ProductDtoList>> getBestSellers(@RequestParam(value = "limit", defaultValue = "10") int limit){
        List<ProductDtoList> products = service.getBestSellers(limit);
        return ResponseEntity.ok(products);
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDtoList> update(@RequestPart("product") @Valid ProductDtoUpdate dto, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        ProductDtoList product = service.update(dto, imageFile);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable UUID id) {
        byte[] image = service.getImageById(id);

        if (image == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).contentLength(image.length).body(image);
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
