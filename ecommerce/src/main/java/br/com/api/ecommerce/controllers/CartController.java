package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.services.CartService;
import br.com.api.ecommerce.config.SecurityConfiguration;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Cart", description = "Endpoints for managing user carts")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class CartController {

    @Autowired
    private CartService service;

    @Operation(summary = "Get user cart", description = "Retrieves the cart for a specific user. The user can access their own cart or an ADMIN can access any user's cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only access their own cart"),
        @ApiResponse(responseCode = "404", description = "Cart not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<CartDtoList> getByUserId(@PathVariable UUID userId){
        Cart cart = service.getByUserId(userId);
        return ResponseEntity.ok(service.entityToDto(cart));
    }

    @Operation(summary = "Clear user cart", description = "Removes all items from the user's cart. The user can clear their own cart or an ADMIN can clear any user's cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only manage their own cart"),
        @ApiResponse(responseCode = "404", description = "Cart not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearUserCart(@PathVariable UUID userId){
        service.clearUserCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Add items to cart", description = "Adds one or more items to the user's cart. The user can add items to their own cart or an ADMIN can add items to any user's cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Items added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only manage their own cart"),
        @ApiResponse(responseCode = "404", description = "Cart or product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addItemsToUserCart(@PathVariable UUID userId, @RequestBody @Valid List<DtoItemRequest> dto){
        service.addItemsToUserCart(userId, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Remove items from cart", description = "Removes one or more items from the user's cart. The user can remove items from their own cart or an ADMIN can remove items from any user's cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Items removed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only manage their own cart"),
        @ApiResponse(responseCode = "404", description = "Cart or product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}/items")
    public ResponseEntity<Void> removeItemsFromUserCart(@PathVariable UUID userId, @RequestBody List<DtoItemRequest> itemsToRemove){
        service.removeItemsFromUserCart(userId, itemsToRemove);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
