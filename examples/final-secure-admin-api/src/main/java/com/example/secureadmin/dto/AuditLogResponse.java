package com.example.secureadmin.dto;

import com.example.secureadmin.model.AuditLog;
import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String action,
        Long actorId,
        String actorEmail,
        Long targetUserId,
        String oldValue,
        String newValue,
        LocalDateTime createdAt
) {

    public static AuditLogResponse from(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getAction(),
                auditLog.getActorId(),
                auditLog.getActorEmail(),
                auditLog.getTargetUserId(),
                auditLog.getOldValue(),
                auditLog.getNewValue(),
                auditLog.getCreatedAt()
        );
    }
}
