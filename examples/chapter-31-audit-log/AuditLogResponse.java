package com.example.backendapi.dto;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String action,
        Long actorId,
        Long targetUserId,
        String oldValue,
        String newValue,
        LocalDateTime createdAt
) {
}

