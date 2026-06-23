package com.example.backendapi.dto;

public record LoginResponse(
        String token,
        UserResponse user
) {
}

