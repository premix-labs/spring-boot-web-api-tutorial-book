package com.example.backendapi.dto;

import com.example.backendapi.model.Role;
import com.example.backendapi.model.UserStatus;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

