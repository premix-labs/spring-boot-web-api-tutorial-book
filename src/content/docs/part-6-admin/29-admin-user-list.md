---
title: 29 - Admin ดูรายชื่อผู้ใช้
description: สร้าง API สำหรับ admin ดูรายชื่อผู้ใช้พร้อม pagination และ filter
---

## เป้าหมายของบท

บทนี้จะสร้าง endpoint สำหรับ admin ดูรายชื่อผู้ใช้ทั้งหมด:

```text
GET /api/v1/admin/users
```

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- admin user list ต่างจาก user list ปกติอย่างไร
- ทำไมต้องมี pagination
- filter ด้วย status/role/keyword ได้อย่างไร
- response ควรใช้ `PageResponse<UserResponse>`
- endpoint นี้ต้องถูกป้องกันด้วย role `ADMIN`

## Query parameter เป้าหมาย

```text
GET /api/v1/admin/users?page=0&size=20&status=ACTIVE&role=USER&keyword=john
```

parameter:

| Parameter | ความหมาย |
| --- | --- |
| `page` | หน้า เริ่มจาก 0 |
| `size` | จำนวนรายการต่อหน้า |
| `status` | `ACTIVE` หรือ `INACTIVE` |
| `role` | `USER` หรือ `ADMIN` |
| `keyword` | ค้นจาก username/email |

## เพิ่ม method ใน UserRepository

เพิ่ม method ใหม่เข้าไปใน `UserRepository` เดิมจากบทก่อนหน้า อย่าลบ method search ของ user list ปกติจากบท 20 เพราะ `UserService` ยังใช้อยู่:

```java
Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String username,
        String email,
        Pageable pageable
);

@Query("""
        select u from User u
        where (:status is null or u.status = :status)
          and (:role is null or u.role = :role)
          and (
                :keyword = ''
                or lower(u.username) like lower(concat('%', :keyword, '%'))
                or lower(u.email) like lower(concat('%', :keyword, '%'))
          )
        """)
Page<User> searchForAdmin(
        UserStatus status,
        Role role,
        String keyword,
        Pageable pageable
);
```

import:

```java
import com.example.backendapi.model.Role;
import com.example.backendapi.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
```

## สร้าง AdminUserService

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/service/AdminUserService.java
```

```java
package com.example.backendapi.service;

import com.example.backendapi.common.PageResponse;
import com.example.backendapi.dto.UserResponse;
import com.example.backendapi.model.Role;
import com.example.backendapi.model.User;
import com.example.backendapi.model.UserStatus;
import com.example.backendapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public PageResponse<UserResponse> findUsers(
            int page,
            int size,
            UserStatus status,
            Role role,
            String keyword
    ) {
        int safeSize = Math.min(size, 100);
        Pageable pageable = PageRequest.of(
                page,
                safeSize,
                Sort.by("createdAt").descending()
        );

        return PageResponse.from(
                userRepository.searchForAdmin(status, role, normalize(keyword), pageable)
                        .map(this::toResponse)
        );
    }

    private String normalize(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return "";
        }
        return keyword.trim();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
```

## สร้าง AdminUserController

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/controller/AdminUserController.java
```

```java
package com.example.backendapi.controller;

import com.example.backendapi.common.ApiResponse;
import com.example.backendapi.common.PageResponse;
import com.example.backendapi.dto.UserResponse;
import com.example.backendapi.model.Role;
import com.example.backendapi.model.UserStatus;
import com.example.backendapi.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
```

## ตัวอย่าง response

```json
{
  "success": true,
  "message": "Users found",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "john",
        "email": "john@example.com",
        "role": "USER",
        "status": "ACTIVE"
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

## แบบฝึกหัดท้ายบท

1. เพิ่ม `searchForAdmin` ใน `UserRepository`
2. สร้าง `AdminUserService`
3. สร้าง `AdminUserController`
4. ทดสอบ filter ด้วย `status=ACTIVE`
5. ทดสอบ filter ด้วย `role=ADMIN`
6. ทดสอบ keyword ที่ค้นจาก email
