package com.example.secureadmin.controller;

import com.example.secureadmin.common.ApiResponse;
import com.example.secureadmin.common.PageResponse;
import com.example.secureadmin.dto.AuditLogResponse;
import com.example.secureadmin.service.AuditLogService;
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
