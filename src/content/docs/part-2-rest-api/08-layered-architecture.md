---
title: 08 - แยก Layer
description: แยก Controller, Service และ Repository ให้ชัดเจน
---

## เป้าหมายของบท

บทนี้จะปรับ User API จากบทก่อนหน้าให้แยก `UserService` ออกจาก `UserController` เพื่อให้โค้ดเริ่มมีโครงสร้างเหมือนงานจริงมากขึ้น

หลังจบบทนี้ flow จะเป็น:

```text
Postman -> UserController -> UserService -> List<User>
```

## ปัญหาของ controller ที่ทำทุกอย่าง

ในบทก่อนหน้า `UserController` ทำหลายหน้าที่พร้อมกัน:

- รับ request
- เก็บ list
- สร้าง id
- ค้นหา user
- ลบ user
- ตัดสินใจว่า user not found

ตอนเริ่มเรียนยังพอรับได้ แต่ถ้าระบบโต controller จะกลายเป็น class ใหญ่ที่อ่านยากและ test ยาก

## สร้าง UserService

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/service/UserService.java
```

```java
package com.example.secureadmin.service;

import com.example.secureadmin.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private Long nextId = 1L;

    public List<User> getUsers() {
        return users;
    }

    public User getUser(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        user.setId(nextId++);
        users.add(user);
        return user;
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        users.remove(user);
    }
}
```

`@Service` บอก Spring ว่า class นี้เป็น bean ประเภท service และสามารถ inject ไปใช้ใน controller ได้

## ปรับ UserController

```java
package com.example.secureadmin.controller;

import com.example.secureadmin.model.User;
import com.example.secureadmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted";
    }
}
```

## @RequiredArgsConstructor ทำอะไร

`@RequiredArgsConstructor` มาจาก Lombok ช่วยสร้าง constructor ให้ field ที่เป็น `final`

โค้ดนี้:

```java
private final UserService userService;
```

จะถูก Lombok สร้าง constructor ให้ Spring inject `UserService` เข้ามา

## ทำไมแบบนี้ดีกว่า

- Controller เหลือหน้าที่รับ request และส่ง response
- Service เก็บ logic การจัดการ user
- ถ้าภายหลังเปลี่ยนจาก `List<User>` เป็น database จะเปลี่ยนใน service/repository ได้ง่ายกว่า
- test service แยกได้โดยไม่ต้องเปิด HTTP server

## ยังขาด Repository หรือไม่

ในบทนี้ยังไม่มี repository เพราะยังไม่ต่อ database แต่ในภาค 3 เราจะเพิ่ม:

```text
UserRepository extends JpaRepository<User, Long>
```

แล้วให้ `UserService` เรียก repository แทน list

## Checkpoint

ทดสอบ endpoint เดิมทั้งหมดอีกครั้ง:

```text
GET    /api/v1/users
GET    /api/v1/users/1
POST   /api/v1/users
DELETE /api/v1/users/1
```

ถ้าผลลัพธ์เหมือนเดิม แปลว่าเรา refactor สำเร็จโดยไม่เปลี่ยน behavior

## แบบฝึกหัดท้ายบท

เพิ่ม method `updateUser(Long id, User request)` ใน `UserService` แล้วเรียกจาก controller ผ่าน `PUT /api/v1/users/{id}`
