package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoUpdate;
import br.com.api.ecommerce.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewDtoList> create(@RequestBody @Valid ReviewDtoCreate dto) {
        ReviewDtoList review = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewDtoList>> getAllByProduct(@PathVariable UUID productId, Pageable pageable){
        Page<ReviewDtoList> reviews = service.getAllByProduct(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping
    public ResponseEntity<ReviewDtoList> update(@RequestBody @Valid ReviewDtoUpdate dto) {
        ReviewDtoList review = service.update(dto);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
