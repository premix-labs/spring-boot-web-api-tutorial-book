package com.example.secureadmin.dto;

import com.example.secureadmin.model.UserStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(
        @NotNull
        UserStatus status
) {
}
