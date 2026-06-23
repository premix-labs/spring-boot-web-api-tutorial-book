---
title: 30 - เปลี่ยน Role และ Status
description: ให้ admin เปลี่ยน role และสถานะผู้ใช้ด้วย PATCH endpoint
---

## เป้าหมายของบท

บทนี้จะเพิ่ม endpoint สำหรับ admin จัดการสิทธิ์และสถานะผู้ใช้:

```text
PATCH /api/v1/admin/users/{id}/role
PATCH /api/v1/admin/users/{id}/status
```

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- ทำไมการเปลี่ยน role/status เป็น action ที่ต้องระวัง
- ใช้ request DTO แยกสำหรับ role/status อย่างไร
- ทำไมควรใช้ `@Transactional`
- ป้องกัน admin ปิดบัญชีตัวเองอย่างไร
- เตรียมจุดเชื่อมกับ audit log อย่างไร

## สร้าง request DTO

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/dto/ChangeRoleRequest.java
```

```java
package com.example.backendapi.dto;

import com.example.backendapi.model.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
        @NotNull(message = "Role is required")
        Role role
) {
}
```

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/dto/ChangeStatusRequest.java
```

```java
package com.example.backendapi.dto;

import com.example.backendapi.model.UserStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(
        @NotNull(message = "Status is required")
        UserStatus status
) {
}
```

## เพิ่ม endpoint ใน AdminUserController

```java
@PatchMapping("/{id}/role")
public ApiResponse<UserResponse> changeRole(
        @PathVariable Long id,
        @Valid @RequestBody ChangeRoleRequest request
) {
    return ApiResponse.ok(
            "User role changed",
            adminUserService.changeRole(id, request)
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
```

`Authentication` ใช้เพื่อรู้ว่า admin คนไหนกำลังทำ action นี้

## เพิ่ม logic ใน AdminUserService

```java
@Transactional
public UserResponse changeRole(Long id, ChangeRoleRequest request) {
    User user = findUser(id);
    user.setRole(request.role());
    return toResponse(user);
}
```

สำหรับ status:

```java
@Transactional
public UserResponse changeStatus(
        Long id,
        ChangeStatusRequest request,
        String actorEmail
) {
    User actor = userRepository.findByEmail(actorEmail)
            .orElseThrow(() -> new BadCredentialsException("Invalid token"));
    User target = findUser(id);

    if (actor.getId().equals(target.getId())
            && request.status() == UserStatus.INACTIVE) {
        throw new IllegalStateException("Admin cannot deactivate own account");
    }

    target.setStatus(request.status());
    return toResponse(target);
}
```

ใช้ `@Transactional` เพราะเราแก้ entity ที่อยู่ใน persistence context แล้วให้ JPA flush ตอน transaction จบ

## helper method

```java
private User findUser(Long id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
}
```

## เพิ่ม handler สำหรับ rule ผิด

ใน `GlobalExceptionHandler` เพิ่ม:

```java
@ExceptionHandler(IllegalStateException.class)
public ResponseEntity<ErrorResponse> handleIllegalState(
        IllegalStateException ex,
        HttpServletRequest request
) {
    return ResponseEntity.badRequest()
            .body(ErrorResponse.of(400, ex.getMessage(), request.getRequestURI()));
}
```

ในงานจริงอาจสร้าง custom exception เช่น `InvalidAdminActionException` แทน `IllegalStateException` เพื่อให้ชัดขึ้น

## Request เปลี่ยน role

```http
PATCH /api/v1/admin/users/5/role
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json
```

```json
{
  "role": "ADMIN"
}
```

## Request เปลี่ยน status

```http
PATCH /api/v1/admin/users/5/status
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json
```

```json
{
  "status": "INACTIVE"
}
```

## Rule ที่ควรมี

- endpoint ต้องเป็น admin-only
- role/status ห้ามเป็น null
- admin ไม่ควร disable ตัวเอง
- ควรระวังการลดสิทธิ์ admin คนสุดท้าย
- ควรบันทึก audit log ทุกครั้ง

บทนี้ยังไม่ลง audit log เพื่อให้เห็น role/status flow ก่อน บทถัดไปจะเพิ่ม audit log เข้าไป

## แบบฝึกหัดท้ายบท

1. สร้าง `ChangeRoleRequest`
2. สร้าง `ChangeStatusRequest`
3. เพิ่ม endpoint เปลี่ยน role
4. เพิ่ม endpoint เปลี่ยน status
5. ทดสอบ user token ว่าเข้าไม่ได้
6. ทดสอบ admin token ว่าเข้าได้
7. ทดสอบ admin ปิดบัญชีตัวเอง
