package com.example.backendapi.dto;

import com.example.backendapi.model.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
        @NotNull
        Role role
) {
}
