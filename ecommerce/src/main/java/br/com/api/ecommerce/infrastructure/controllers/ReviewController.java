package br.com.api.ecommerce.infrastructure.controllers;

import br.com.api.ecommerce.application.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.application.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.application.dtos.Review.ReviewDtoUpdate;
import br.com.api.ecommerce.infrastructure.config.SecurityConfiguration;
import br.com.api.ecommerce.services.ReviewService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "Endpoints for managing reviews")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    @Operation(summary = "Create a new review", description = "Creates a new review for a product. Requires the user to have purchased and received the product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Review created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied - user must have purchased the product"),
        @ApiResponse(responseCode = "409", description = "User has already reviewed this product"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewDtoList> create(@RequestBody @Valid ReviewDtoCreate dto) {
        ReviewDtoList review = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all reviews for a product", description = "Retrieves all reviews for a specific product with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ReviewDtoList>> getAllByProduct(@PathVariable UUID productId, Pageable pageable){
        Page<ReviewDtoList> reviews = service.getAllByProduct(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping
    @Operation(summary = "Update an existing review", description = "Updates an existing review. Only the review owner or an admin can update a review.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied - only review owner or admin can update"),
        @ApiResponse(responseCode = "404", description = "Review not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewDtoList> update(@RequestBody @Valid ReviewDtoUpdate dto) {
        ReviewDtoList review = service.update(dto);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Deletes a review. Only the review owner or an admin can delete a review.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - only review owner or admin can delete"),
        @ApiResponse(responseCode = "404", description = "Review not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
