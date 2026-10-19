package vn.edu.uit.da2.exchange.controller;

import vn.edu.uit.da2.exchange.dto.RegisterRequest;
import vn.edu.uit.da2.exchange.dto.UserResponse;
import vn.edu.uit.da2.exchange.service.UserService;

import jakarta.validation.Valid;

import vn.edu.uit.da2.exchange.dto.LoginRequest;
import vn.edu.uit.da2.exchange.dto.LoginResponse;
import jakarta.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request) {

    LoginResponse response = userService.login(request);

    return ResponseEntity.ok(response);
}
}
