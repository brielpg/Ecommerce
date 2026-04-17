package br.com.api.ecommerce.infrastructure.controllers;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.dtos.User.UserDtoList;
import br.com.api.ecommerce.application.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.application.mappers.UserMapper;
import br.com.api.ecommerce.application.usecases.user.*;
import br.com.api.ecommerce.domain.models.User;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for managing users")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
@RequiredArgsConstructor
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final RestoreUserUseCase restoreUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserFavoritesUseCase getUserFavoritesUseCase;
    private final AddUserFavoriteUseCase addUserFavoriteUseCase;
    private final RemoveUserFavoriteUseCase removeUserFavoriteUseCase;
    private final UserMapper mapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users with pagination support. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<UserDtoList>> getAll(Pageable pageable){
        Page<UserDtoList> users = getAllUsersUseCase.execute(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by ID. The user can access their own data or an ADMIN can access any user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only access their own data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDtoList> getById(@PathVariable UUID id){
        User user = getUserByIdUseCase.execute(id);
        UserDtoList userDtoList = mapper.toDto(user);
        return ResponseEntity.ok(userDtoList);
    }

    @PutMapping
    @Operation(summary = "Update user", description = "Updates user information. The user can update their own data or an ADMIN can update any user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or current password is incorrect"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only update their own data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email already registered"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDtoList> update(@RequestBody @Valid UserDtoUpdate dto){
        UserDtoList user = updateUserUseCase.execute(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore user", description = "Restores a previously deleted (deactivated) user account. The user can restore their own account or an ADMIN can restore any user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User restored successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only restore their own account"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> restore(@PathVariable UUID id){
        restoreUserUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Soft deletes (deactivates) a user account. The user can delete their own account or an ADMIN can delete any user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only delete their own account"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        deleteUserUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/favorites")
    @Operation(summary = "Get user favorites", description = "Retrieves all favorite products for a user. The user can view their own favorites or an ADMIN can view any user's favorites.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only view their own favorites"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductDtoList>> getFavorites(@PathVariable UUID id){
        List<ProductDtoList> favorites = getUserFavoritesUseCase.execute(id);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{id}/favorites/{productId}")
    @Operation(summary = "Add product to favorites", description = "Adds a product to the user's favorites list. The user can add favorites to their own list or an ADMIN can add favorites for any user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product added to favorites successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only manage their own favorites"),
        @ApiResponse(responseCode = "404", description = "User or product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> addFavorite(@PathVariable UUID id, @PathVariable UUID productId){
        addUserFavoriteUseCase.execute(id, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/favorites/{productId}")
    @Operation(summary = "Remove product from favorites", description = "Removes a product from the user's favorites list. The user can remove favorites from their own list or an ADMIN can remove favorites for any user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product removed from favorites successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - user can only manage their own favorites"),
        @ApiResponse(responseCode = "404", description = "User or product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> removeFavorite(@PathVariable UUID id, @PathVariable UUID productId){
        removeUserFavoriteUseCase.execute(id, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
