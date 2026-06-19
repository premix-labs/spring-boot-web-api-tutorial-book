package com.example.secureadmin.dto;

import com.example.secureadmin.model.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
        @NotNull(message = "Role is required")
        Role role
) {
}

