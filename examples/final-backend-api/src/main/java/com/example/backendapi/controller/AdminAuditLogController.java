package com.example.backendapi.controller;

import com.example.backendapi.common.ApiResponse;
import com.example.backendapi.common.PageResponse;
import com.example.backendapi.dto.AuditLogResponse;
import com.example.backendapi.service.AuditLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
public class AdminAuditLogController {

    private final AuditLogService auditLogService;

    public AdminAuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    ApiResponse<PageResponse<AuditLogResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ApiResponse.ok(
                "Audit logs loaded successfully",
                PageResponse.from(auditLogService.findAll(pageable))
        );
    }
}
