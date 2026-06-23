package com.example.backendapi.controller;

import com.example.backendapi.common.ApiResponse;
import com.example.backendapi.common.PageResponse;
import com.example.backendapi.dto.ChangeRoleRequest;
import com.example.backendapi.dto.ChangeStatusRequest;
import com.example.backendapi.dto.UserResponse;
import com.example.backendapi.model.Role;
import com.example.backendapi.model.User;
import com.example.backendapi.model.UserStatus;
import com.example.backendapi.service.AdminUserService;
import com.example.backendapi.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
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
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final CurrentUserService currentUserService;

    public AdminUserController(
            AdminUserService adminUserService,
            CurrentUserService currentUserService
    ) {
        this.adminUserService = adminUserService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    ApiResponse<PageResponse<UserResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) UserStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ApiResponse.ok(
                "Users loaded successfully",
                PageResponse.from(adminUserService.search(keyword, role, status, pageable))
        );
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok("User loaded successfully", adminUserService.findById(id));
    }

    @PatchMapping("/{id}/role")
    ApiResponse<UserResponse> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request,
            Authentication authentication
    ) {
        User actor = currentUserService.getRequiredActiveUser(authentication);
        return ApiResponse.ok(
                "User role changed successfully",
                adminUserService.changeRole(id, request.role(), actor)
        );
    }

    @PatchMapping("/{id}/status")
    ApiResponse<UserResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeStatusRequest request,
            Authentication authentication
    ) {
        User actor = currentUserService.getRequiredActiveUser(authentication);
        return ApiResponse.ok(
                "User status changed successfully",
                adminUserService.changeStatus(id, request.status(), actor)
        );
    }
}
