---
title: 19 - Response Format
description: ออกแบบรูปแบบ response กลางให้ client อ่านผลลัพธ์ได้สม่ำเสมอ
---

## เป้าหมายของบท

บทนี้จะเพิ่ม response wrapper ชื่อ `ApiResponse<T>` เพื่อให้ success response ของระบบมีรูปแบบสม่ำเสมอ

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- response format กลางช่วยอะไร
- ควรใช้ wrapper เมื่อไหร่
- วิธีสร้าง generic record `ApiResponse<T>`
- วิธีใช้ `ResponseEntity` คู่กับ `ApiResponse`
- ข้อควรระวังของการห่อ response ทุก endpoint

## ปัญหาของ response ที่ไม่สม่ำเสมอ

ถ้าแต่ละ endpoint ตอบไม่เหมือนกัน client จะเขียนโค้ดยาก

ตัวอย่าง response แบบไม่สม่ำเสมอ:

```json
[
  {
    "id": 1,
    "email": "john@example.com"
  }
]
```

อีก endpoint:

```json
{
  "message": "User created",
  "user": {
    "id": 1,
    "email": "john@example.com"
  }
}
```

อีก endpoint:

```json
{
  "data": {
    "id": 1,
    "email": "john@example.com"
  }
}
```

ถ้า response shape เปลี่ยนไปเรื่อย ๆ frontend ต้องเช็กหลายรูปแบบ ทำให้ bug ง่าย

## สร้าง ApiResponse

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/common/ApiResponse.java
```

```java
package com.example.backendapi.common;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now());
    }
}
```

`<T>` ทำให้ `data` เป็น type อะไรก็ได้ เช่น:

```text
UserResponse
List<UserResponse>
PageResponse<UserResponse>
```

## ใช้กับ GET endpoint

```java
@GetMapping("/{id}")
public ApiResponse<UserResponse> findById(@PathVariable Long id) {
    return ApiResponse.ok("User found", userService.findById(id));
}
```

response:

```json
{
  "success": true,
  "message": "User found",
  "data": {
    "id": 1,
    "username": "john",
    "email": "john@example.com",
    "role": "USER",
    "status": "ACTIVE"
  },
  "timestamp": "2026-06-05T15:30:00"
}
```

## ใช้กับ POST endpoint

กรณี create ควรใช้ `ResponseEntity` เพื่อกำหนด status `201 Created`:

```java
@PostMapping
public ResponseEntity<ApiResponse<UserResponse>> create(
        @Valid @RequestBody CreateUserRequest request
) {
    UserResponse user = userService.create(request);
    URI location = URI.create("/api/v1/users/" + user.id());

    return ResponseEntity.created(location)
            .body(ApiResponse.ok("User created", user));
}
```

จุดสำคัญ:

- HTTP status เป็น `201 Created`
- header `Location` ชี้ไปยัง resource ใหม่
- body ใช้ format กลาง

## ใช้กับ DELETE endpoint

สำหรับ delete มี 2 แนวทางที่ใช้ได้

แนวทางแรก ตอบ `204 No Content` และไม่มี body:

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
}
```

แนวทางที่สอง ตอบ `200 OK` พร้อม message:

```java
@DeleteMapping("/{id}")
public ApiResponse<Void> deleteById(@PathVariable Long id) {
    userService.deleteById(id);
    return ApiResponse.ok("User deleted", null);
}
```

ในหนังสือนี้จะใช้ `204 No Content` ต่อ เพราะเป็น REST convention ที่ชัดเจน และไม่จำเป็นต้องมี body

## Error response ควรรวมกับ ApiResponse ไหม

ทำได้ แต่ในหนังสือนี้จะแยก:

- success response ใช้ `ApiResponse<T>`
- error response ใช้ `ErrorResponse`

เหตุผลคือ error มักมี field เฉพาะ เช่น `status`, `path`, `errors` ส่วน success response เน้น `message` และ `data`

## อย่าออกแบบ wrapper ซับซ้อนเกินไป

มือใหม่มักอยากใส่ field เยอะ เช่น:

```text
success
code
message
data
meta
links
debug
trace
requestId
timestamp
version
```

เริ่มจากน้อยก่อน:

```text
success
message
data
timestamp
```

แล้วเพิ่มเมื่อมีความต้องการจริง

## แบบฝึกหัดท้ายบท

1. สร้าง `ApiResponse<T>`
2. ปรับ `GET /api/v1/users/{id}` ให้ใช้ `ApiResponse`
3. ปรับ `POST /api/v1/users` ให้ตอบ `201 Created` พร้อม `ApiResponse`
4. ตัดสินใจว่าจะให้ `DELETE` ตอบ `204` หรือ `ApiResponse`
5. เทียบ response ก่อนและหลังเพิ่ม wrapper

