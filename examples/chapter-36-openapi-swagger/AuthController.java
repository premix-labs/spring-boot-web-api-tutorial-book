package com.example.backendapi.controller;

import com.example.backendapi.common.ApiResponse;
import com.example.backendapi.dto.LoginRequest;
import com.example.backendapi.dto.LoginResponse;
import com.example.backendapi.dto.RegisterRequest;
import com.example.backendapi.dto.UserResponse;
import com.example.backendapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ApiResponse.ok("User registered", authService.register(request));
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ApiResponse.ok("Login successful", authService.login(request));
    }

    @Operation(summary = "Get current user from JWT")
    @GetMapping("/me")
    public ApiResponse<UserResponse> me(Authentication authentication) {
        return ApiResponse.ok(
                "Current user found",
                authService.getCurrentUser(authentication.getName())
        );
    }
}

