package br.com.api.ecommerce.infrastructure.controllers;

import br.com.api.ecommerce.infrastructure.config.SecurityConfiguration;
import br.com.api.ecommerce.application.dtos.Auth.AuthDtoLogin;
import br.com.api.ecommerce.application.dtos.Auth.AuthReturnToken;
import br.com.api.ecommerce.application.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.services.AuthorizationService;
import br.com.api.ecommerce.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthReturnToken> login(@RequestBody @Valid AuthDtoLogin dto){
        AuthReturnToken token = authorizationService.login(dto);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "User registration", description = "Registers a new user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registration successful"),
        @ApiResponse(responseCode = "409", description = "Email already registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthReturnToken> register(@RequestBody @Valid UserDtoCreate dto){
        userService.create(dto);
        AuthReturnToken token = login(new AuthDtoLogin(dto.email(), dto.password())).getBody();
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}
