package com.example.secureadmin.controller;

import com.example.secureadmin.common.ApiResponse;
import com.example.secureadmin.common.PageResponse;
import com.example.secureadmin.dto.ChangeRoleRequest;
import com.example.secureadmin.dto.ChangeStatusRequest;
import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.model.Role;
import com.example.secureadmin.model.UserStatus;
import com.example.secureadmin.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> findUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponse.ok(
                "Users found",
                adminUserService.findUsers(page, size, status, role, keyword)
        );
    }

    @PatchMapping("/{id}/role")
    public ApiResponse<UserResponse> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request,
            Authentication authentication
    ) {
        return ApiResponse.ok(
                "User role changed",
                adminUserService.changeRole(id, request, authentication.getName())
        );
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<UserResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeStatusRequest request,
            Authentication authentication
    ) {
        return ApiResponse.ok(
                "User status changed",
                adminUserService.changeStatus(id, request, authentication.getName())
        );
    }
}
