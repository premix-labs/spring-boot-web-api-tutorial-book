---
title: 20 - Pagination, Sorting, Filtering
description: ดึงข้อมูลเป็นหน้า จัดเรียง และค้นหาข้อมูลผู้ใช้ด้วย query parameter
---

## เป้าหมายของบท

บทนี้จะปรับ endpoint รายชื่อผู้ใช้ให้รองรับข้อมูลจำนวนมาก ด้วย pagination, sorting และ filtering

หลังจบบทนี้ endpoint รายชื่อผู้ใช้ควรรองรับ query parameter แบบนี้:

```text
GET /api/v1/users?page=0&size=20&sortBy=createdAt&direction=desc&keyword=john
```

ผู้อ่านควรเข้าใจ:

- ทำไมไม่ควรใช้ `findAll()` กับข้อมูลจำนวนมาก
- ใช้ `Pageable`, `PageRequest`, `Sort` อย่างไร
- ส่ง pagination metadata ให้ client อย่างไร
- filtering เบื้องต้นด้วย keyword ทำอย่างไร
- จำกัด `size` เพื่อป้องกัน request ใหญ่เกินไป

## ทำไม findAll ไม่พอ

ตอนมี user 10 คน `findAll()` ยังไม่เป็นปัญหา

แต่ถ้ามี user 100,000 คน การส่งข้อมูลทั้งหมดกลับไปใน request เดียวจะทำให้:

- database ทำงานหนัก
- backend ใช้ memory มาก
- network ช้า
- frontend render ช้า
- user experience แย่

ดังนั้น endpoint list ควรดึงเป็นหน้า

## รูปแบบ query parameter

เราจะใช้:

| Parameter | ตัวอย่าง | ความหมาย |
| --- | --- | --- |
| `page` | `0` | เลขหน้า เริ่มจาก 0 |
| `size` | `20` | จำนวนรายการต่อหน้า |
| `sortBy` | `createdAt` | field ที่ใช้จัดเรียง |
| `direction` | `desc` | เรียงจากมากไปน้อยหรือจากน้อยไปมาก |
| `keyword` | `john` | ค้นหา username/email |

ตัวอย่าง:

```http
GET /api/v1/users?page=0&size=10&sortBy=username&direction=asc&keyword=jo
```

## สร้าง PageResponse

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/common/PageResponse.java
```

```java
package com.example.secureadmin.common;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
```

`PageResponse` ทำให้ frontend รู้ว่า:

- ตอนนี้อยู่หน้าไหน
- มีทั้งหมดกี่หน้า
- มีทั้งหมดกี่ record
- เป็นหน้าแรกหรือหน้าสุดท้ายไหม

## เพิ่ม method ใน UserRepository

เปิด `UserRepository` แล้วเพิ่ม method ค้นหาด้วย keyword:

```java
Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String username,
        String email,
        Pageable pageable
);
```

พร้อม import:

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
```

method นี้ค้นหา username หรือ email ที่มี keyword โดยไม่สนตัวพิมพ์เล็กใหญ่

## ปรับ UserService

เพิ่ม method:

```java
public PageResponse<UserResponse> findAll(
        int page,
        int size,
        String sortBy,
        String direction,
        String keyword
) {
    int safeSize = Math.min(size, 100);
    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, safeSize, sort);
    Page<User> users;

    if (keyword == null || keyword.isBlank()) {
        users = userRepository.findAll(pageable);
    } else {
        users = userRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                );
    }

    return PageResponse.from(users.map(this::toResponse));
}
```

จำกัด `size` ไม่เกิน 100 เพื่อป้องกัน client ส่ง `size=100000`

## ปรับ UserController

```java
@GetMapping
public ApiResponse<PageResponse<UserResponse>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction,
        @RequestParam(required = false) String keyword
) {
    PageResponse<UserResponse> users = userService.findAll(
            page,
            size,
            sortBy,
            direction,
            keyword
    );

    return ApiResponse.ok("Users found", users);
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
  },
  "timestamp": "2026-06-05T15:30:00"
}
```

## ควรระวัง sortBy

ตัวอย่างนี้รับ `sortBy` จาก client ตรง ๆ เพื่อให้เห็น flow ง่าย

ในงานจริงควร whitelist field ที่อนุญาต เช่น:

```text
id
username
email
createdAt
```

เพราะถ้า client ส่ง field ที่ไม่มีอยู่ เช่น `sortBy=abc` อาจเกิด error จาก JPA

## Checkpoint ปิดภาค Better API

หลังจบบทนี้ API ควรมีคุณภาพดีขึ้น:

- validate request body
- มี error response กลาง
- ใช้ DTO แยกจาก entity
- success response สม่ำเสมอ
- list endpoint รองรับ pagination, sorting, filtering

นี่คือฐานที่ดีสำหรับเข้าสู่ภาค Login/Register เพราะ auth API ต้องการ validation, error handling และ DTO ที่ชัดเจน

## แบบฝึกหัดท้ายบท

1. เพิ่ม `PageResponse`
2. ปรับ `GET /api/v1/users` ให้รับ `page` และ `size`
3. เพิ่ม sorting ด้วย `sortBy` และ `direction`
4. เพิ่ม keyword search ด้วย username/email
5. จำกัด `size` สูงสุดไม่เกิน 100
6. ทดสอบ keyword ที่ไม่เจอข้อมูล

