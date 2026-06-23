package com.example.backendapi.dto;

import com.example.backendapi.model.UserStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(
        @NotNull(message = "Status is required")
        UserStatus status
) {
}

