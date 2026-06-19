package com.example.secureadmin.dto;

public record LoginResponse(
        String token,
        UserResponse user
) {
}

