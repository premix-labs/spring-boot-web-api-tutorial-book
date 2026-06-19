package com.example.secureadmin.service;

import com.example.secureadmin.common.PageResponse;
import com.example.secureadmin.dto.AuditLogResponse;
import com.example.secureadmin.model.AuditLog;
import com.example.secureadmin.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void record(
            String action,
            Long actorId,
            Long targetUserId,
            String oldValue,
            String newValue
    ) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setActorId(actorId);
        log.setTargetUserId(targetUserId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        auditLogRepository.save(log);
    }

    public PageResponse<AuditLogResponse> findAll(int page, int size) {
        int safeSize = Math.min(size, 100);
        return PageResponse.from(
                auditLogRepository.findAll(
                        PageRequest.of(page, safeSize, Sort.by("createdAt").descending())
                ).map(this::toResponse)
        );
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getAction(),
                log.getActorId(),
                log.getTargetUserId(),
                log.getOldValue(),
                log.getNewValue(),
                log.getCreatedAt()
        );
    }
}

