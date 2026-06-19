---
title: 16 - Validation
description: ตรวจ request ด้วย Bean Validation ก่อนส่งต่อให้ service
---

## เป้าหมายของบท

บทนี้จะเพิ่ม validation ให้ API เพื่อป้องกันข้อมูลไม่ถูกต้องตั้งแต่ขอบระบบ ก่อนที่ข้อมูลจะไหลเข้า service และ database

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- ทำไม validation ควรอยู่ที่ request DTO
- ใช้ `@Valid` ใน controller อย่างไร
- ใช้ `@NotBlank`, `@Email`, `@Size` อย่างไร
- ต้องเพิ่ม dependency อะไรเพื่อให้ Bean Validation ทำงาน
- validation ช่วยลด error แปลก ๆ จาก database ได้อย่างไร

## ทำไมต้อง validate request

ถ้าไม่ validate ตั้งแต่ต้น API อาจรับข้อมูลแบบนี้เข้าไป:

```json
{
  "username": "",
  "email": "not-email",
  "password": "123"
}
```

ผลเสียคือ:

- service ต้องรับมือข้อมูลไม่ครบ
- database อาจ error เพราะ `nullable = false`
- client ได้ error ที่อ่านยาก
- business rule กระจัดกระจาย

validation ทำให้ API ปฏิเสธ request ที่ผิดตั้งแต่ต้น และส่งข้อความที่ client เข้าใจได้มากขึ้น

## เพิ่ม dependency validation

เปิดไฟล์ `build.gradle` แล้วเพิ่ม:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

ถ้าไม่มี dependency นี้ annotation อย่าง `@NotBlank` และ `@Email` อาจไม่ทำงาน

## สร้าง CreateUserRequest

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/dto/CreateUserRequest.java
```

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

## สร้าง UpdateUserRequest

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/dto/UpdateUserRequest.java
```

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

ใน update endpoint ยังไม่รับ password เพราะ password ควรมี flow แยก เช่น change password หรือ reset password

## ใส่ @Valid ใน Controller

เปิด `UserController` แล้วแก้ endpoint ที่รับ request body:

```java
@PostMapping
public ResponseEntity<UserResponse> create(
        @Valid @RequestBody CreateUserRequest request
) {
    User user = userService.create(request);
    URI location = URI.create("/api/v1/users/" + user.getId());
    return ResponseEntity.created(location).body(toResponse(user));
}
```

สำหรับ update:

```java
@PutMapping("/{id}")
public UserResponse update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
) {
    return toResponse(userService.update(id, request));
}
```

ต้อง import:

```java
import jakarta.validation.Valid;
```

## ปรับ Service ให้รับ DTO

เมื่อต้องการใช้ request DTO service จะรับ `CreateUserRequest` และ `UpdateUserRequest` แทน entity:

```java
public User create(CreateUserRequest request) {
    if (userRepository.existsByUsername(request.username())) {
        throw new RuntimeException("Username already exists");
    }

    if (userRepository.existsByEmail(request.email())) {
        throw new RuntimeException("Email already exists");
    }

    User user = new User();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPassword(request.password());

    return userRepository.save(user);
}
```

record ใช้ method ชื่อเดียวกับ field เช่น `request.username()` ไม่ใช่ `request.getUsername()`

## ทดสอบ validation

ลองส่ง request ที่ผิด:

```http
POST /api/v1/users
Content-Type: application/json
```

```json
{
  "username": "",
  "email": "wrong",
  "password": "123"
}
```

ตอนนี้ Spring Boot จะ reject request ก่อนเข้า service

response อาจยังไม่สวย เพราะเรายังไม่ได้เขียน global exception handler สำหรับ validation error บทถัดไปจะทำให้ error response อ่านง่ายขึ้น

## Annotation ที่ใช้บ่อย

| Annotation | ใช้กับอะไร |
| --- | --- |
| `@NotNull` | ต้องไม่เป็น `null` แต่ string ว่างยังผ่าน |
| `@NotBlank` | string ต้องไม่เป็น `null`, ไม่ว่าง และไม่ใช่ช่องว่างล้วน |
| `@Email` | ต้องเป็น email format |
| `@Size` | จำกัดความยาว string หรือขนาด collection |
| `@Min` / `@Max` | จำกัดค่าตัวเลข |
| `@Positive` | ต้องเป็นเลขบวก |

สำหรับ string ที่ user กรอก เช่น username, email, password มักเริ่มจาก `@NotBlank`

## ปัญหาที่พบบ่อย

| อาการ | วิธีแก้ |
| --- | --- |
| validation ไม่ทำงาน | ตรวจว่ามี `spring-boot-starter-validation` |
| ใส่ annotation แล้ว request ยังผ่าน | ตรวจว่า controller ใส่ `@Valid` หรือยัง |
| ใช้ record แล้วเรียก getter ไม่ได้ | record ใช้ `request.username()` |
| error response อ่านยาก | ทำ `GlobalExceptionHandler` ในบทถัดไป |

## แบบฝึกหัดท้ายบท

1. เพิ่ม `CreateUserRequest`
2. เพิ่ม `UpdateUserRequest`
3. ใส่ `@Valid` ใน `POST` และ `PUT`
4. ทดสอบ email ผิด format
5. ทดสอบ password น้อยกว่า 8 ตัวอักษร

