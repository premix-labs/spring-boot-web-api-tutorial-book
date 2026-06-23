package com.example.backendapi.controller;

import com.example.backendapi.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ApiResponse<String> dashboard() {
        return ApiResponse.ok("Admin dashboard", "Only ADMIN can access this endpoint");
    }
}

