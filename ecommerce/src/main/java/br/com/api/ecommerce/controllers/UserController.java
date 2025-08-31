package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Page<UserDtoList>> getAll(Pageable pageable){
        Page<UserDtoList> users = service.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id){
        User user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody @Valid UserDtoUpdate dto){
        User user = service.update(dto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
