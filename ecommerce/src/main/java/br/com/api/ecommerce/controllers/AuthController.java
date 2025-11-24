package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.dtos.Auth.AuthDtoLogin;
import br.com.api.ecommerce.models.dtos.Auth.AuthReturnToken;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.services.AuthorizationService;
import br.com.api.ecommerce.services.UserService;
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
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<AuthReturnToken> login(@RequestBody @Valid AuthDtoLogin dto){
        AuthReturnToken token = authorizationService.login(dto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthReturnToken> register(@RequestBody @Valid UserDtoCreate dto){
        userService.create(dto);
        AuthReturnToken token = login(new AuthDtoLogin(dto.email(), dto.password())).getBody();
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}
