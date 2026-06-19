---
title: 18 - DTO
description: แยก Entity ออกจาก Request และ Response เพื่อให้ API contract ชัดเจน
---

## เป้าหมายของบท

บทนี้จะทำให้ API ไม่รับหรือส่ง entity ตรง ๆ อีกต่อไป โดยใช้ DTO เป็น contract ของ API

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- Entity กับ DTO ต่างกันอย่างไร
- ทำไมไม่ควรรับ `User` entity จาก request body
- ทำไมไม่ควรส่ง `User` entity กลับเป็น response
- วิธีสร้าง `CreateUserRequest`, `UpdateUserRequest`, `UserResponse`
- วิธี map ระหว่าง DTO และ entity

## Entity กับ DTO ต่างกันอย่างไร

Entity คือ model ของ database

DTO คือ model ของ API

เปรียบเทียบ:

| ประเภท | หน้าที่ | ตัวอย่าง |
| --- | --- | --- |
| Entity | map กับ table ใน database | `User` |
| Request DTO | รับข้อมูลจาก client | `CreateUserRequest` |
| Response DTO | ส่งข้อมูลกลับให้ client | `UserResponse` |

ถ้าใช้ entity เป็น request/response ตรง ๆ API จะผูกกับ database มากเกินไป และเสี่ยงเปิด field ที่ไม่ควรเปิด เช่น `password`

## DTO ที่จะใช้ในบทนี้

เราจะใช้ DTO 3 ตัว:

```text
CreateUserRequest
UpdateUserRequest
UserResponse
```

สองตัวแรกเป็น request DTO

ตัวสุดท้ายเป็น response DTO

## CreateUserRequest

```java
package com.example.secureadmin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {
}
```

## UpdateUserRequest

```java
package com.example.secureadmin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        String email
) {
}
```

## UserResponse

```java
package com.example.secureadmin.dto;

import com.example.secureadmin.model.Role;
import com.example.secureadmin.model.UserStatus;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
```

`UserResponse` ไม่มี password เพราะ password ไม่ควรถูกส่งออกจาก API

## สร้าง mapper method

ในช่วงเริ่มต้น เราจะใช้ private method ใน service ก่อน:

```java
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
```

ถ้าโปรเจกต์โตขึ้น อาจแยกเป็น `UserMapper` หรือใช้ library เช่น MapStruct ได้ แต่สำหรับหนังสือเล่มนี้เราจะเริ่มจาก method ธรรมดาเพื่อให้เห็น flow ชัด

## ปรับ Service ให้ return DTO

ตัวอย่าง:

```java
public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
}
```

หา user ตาม id:

```java
public UserResponse findById(Long id) {
    return toResponse(findEntityById(id));
}
```

สร้าง user:

```java
public UserResponse create(CreateUserRequest request) {
    User user = new User();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPassword(request.password());

    return toResponse(userRepository.save(user));
}
```

## ปรับ Controller ให้อ่านง่ายขึ้น

เมื่อ service return DTO แล้ว controller จะไม่ต้อง map เอง:

```java
@GetMapping("/{id}")
public UserResponse findById(@PathVariable Long id) {
    return userService.findById(id);
}
```

ข้อดีคือ controller เหลือหน้าที่หลักคือรับ request และส่ง response

## ข้อดีของ DTO

- ซ่อน field ที่ไม่ควรส่ง เช่น `password`
- แยก API contract ออกจาก database schema
- ใส่ validation ที่ request DTO ได้
- เปลี่ยน database ภายในได้โดยไม่กระทบ response ทันที
- ทำให้ frontend เห็น response shape ชัดเจน

## ข้อควรระวัง

DTO มากเกินไปอาจทำให้โปรเจกต์รก ถ้า endpoint ยังเล็กมากให้สร้างเท่าที่จำเป็นก่อน

แต่สำหรับ `User` ที่มี password, role และ status การแยก DTO เป็นเรื่องที่ควรทำตั้งแต่ต้น

## แบบฝึกหัดท้ายบท

1. สร้าง `UserResponse`
2. ปรับ `UserService` ให้ return `UserResponse`
3. ปรับ `UserController` ให้ไม่ส่ง `User` entity กลับตรง ๆ
4. ทดสอบว่า response ไม่มี `password`
5. ลองเพิ่ม field ใน entity แล้วตรวจว่า response ไม่เปลี่ยนถ้าไม่ได้เพิ่มใน DTO

