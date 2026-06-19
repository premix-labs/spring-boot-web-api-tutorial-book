package com.example.secureadmin.dto;

import com.example.secureadmin.model.Role;
import com.example.secureadmin.model.UserStatus;
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

