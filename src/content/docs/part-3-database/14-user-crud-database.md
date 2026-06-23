---
title: 14 - User CRUD กับฐานข้อมูล
description: ทำ CRUD API โดยใช้ PostgreSQL จริงผ่าน Service และ Repository
---

## เป้าหมายของบท

บทนี้จะเปลี่ยน User API จากการใช้ `List<User>` ใน memory ไปใช้ PostgreSQL จริง

หลังจบบทนี้ผู้อ่านควรมี endpoint:

```text
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
DELETE /api/v1/users/{id}
```

บทที่ 15 จะเติม `PUT /api/v1/users/{id}` สำหรับแก้ไขข้อมูล

## ภาพรวม flow

เมื่อ client เรียก API flow จะเป็น:

```text
Postman
  -> UserController
  -> UserService
  -> UserRepository
  -> PostgreSQL
```

Controller รับ request และส่ง response

Service จัดการ logic เช่น เช็ก email ซ้ำ สร้าง user และหา user

Repository ติดต่อ database

## สร้าง UserService

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/service/UserService.java
```

```java
package com.example.backendapi.service;

import com.example.backendapi.model.User;
import com.example.backendapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User create(User request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
```

ในบทนี้ยังใช้ `RuntimeException` เพื่อให้ flow อ่านง่ายก่อน บท `17 - Exception Handling` จะปรับให้ error response สวยและชัดเจนขึ้น

## สร้าง UserController

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/controller/UserController.java
```

```java
package com.example.backendapi.controller;

import com.example.backendapi.model.Role;
import com.example.backendapi.model.User;
import com.example.backendapi.model.UserStatus;
import com.example.backendapi.service.UserService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return toResponse(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody User request) {
        User user = userService.create(request);
        URI location = URI.create("/api/v1/users/" + user.getId());
        return ResponseEntity.created(location).body(toResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    private record UserResponse(
            Long id,
            String username,
            String email,
            Role role,
            UserStatus status
    ) {
    }
}
```

จุดสำคัญคือ response ไม่ส่ง `password` กลับไป

แม้บท DTO จะอยู่ภาคถัดไป แต่เราจะเริ่มแยก response แบบเล็กที่สุดตั้งแต่ตรงนี้ เพราะ password เป็นข้อมูลที่ไม่ควรถูกส่งออกจาก API

## ทดสอบสร้าง user

ส่ง request:

```http
POST /api/v1/users
Content-Type: application/json
```

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

response ที่ควรได้:

```json
{
  "id": 1,
  "username": "john",
  "email": "john@example.com",
  "role": "USER",
  "status": "ACTIVE"
}
```

สังเกตว่าไม่มี `password` ใน response

## ทดสอบอ่านข้อมูล

ดึง user ทั้งหมด:

```http
GET /api/v1/users
```

ดึง user ตาม id:

```http
GET /api/v1/users/1
```

## ทดสอบลบข้อมูล

```http
DELETE /api/v1/users/1
```

ถ้าลบสำเร็จควรได้:

```text
204 No Content
```

ถ้าลองดึง user id เดิมอีกครั้ง ในตอนนี้จะได้ error แบบ default ของ Spring Boot เพราะเรายังไม่ได้ทำ exception handler

## ข้อจำกัดของบทนี้

โค้ดในบทนี้ยังไม่ใช่ final production version เพราะ:

- ยังรับ request ด้วย entity ตรง ๆ
- ยังใช้ `RuntimeException`
- ยังไม่ได้ validate request body
- password ยังไม่ได้ hash
- ยังไม่มี pagination

ข้อจำกัดเหล่านี้ตั้งใจเว้นไว้ เพราะจะถูกแก้ทีละเรื่องในภาคถัดไป

## แบบฝึกหัดท้ายบท

1. สร้าง user 2 คน
2. เรียก `GET /api/v1/users`
3. ลบ user คนแรก
4. เรียก `GET /api/v1/users` อีกครั้ง
5. ตรวจใน PostgreSQL ว่า row ถูกลบจริง

