---
title: 28 - Admin Only Endpoints
description: จำกัด endpoint สำหรับ admin ด้วย Spring Security
---

## เป้าหมายของบท

บทนี้จะสร้าง admin endpoint ตัวแรก และตั้งค่า Spring Security ให้เฉพาะ role `ADMIN` เท่านั้นที่เรียกได้

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- path `/api/v1/admin/**` ควรใช้กับ endpoint แบบไหน
- ใช้ `hasRole("ADMIN")` อย่างไร
- ความต่างระหว่าง `401 Unauthorized` และ `403 Forbidden`
- วิธีทดสอบด้วย token ของ `USER` และ `ADMIN`

## Endpoint เป้าหมาย

```text
GET /api/v1/admin/dashboard
```

endpoint นี้ใช้ทดสอบสิทธิ์ admin ก่อนเข้าสู่ระบบจัดการผู้ใช้จริงในบทถัดไป

## สร้าง AdminController

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/controller/AdminController.java
```

```java
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
```

## ตั้งค่า SecurityConfig

ให้ admin path ต้องเป็น `ADMIN`:

```java
.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
)
```

ลำดับมีผล ควรวาง rule เฉพาะก่อน `anyRequest()`

## ตรวจ JwtAuthenticationConverter

ถ้ายังไม่มี converter ให้เพิ่ม:

```java
@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    JwtAuthenticationConverter jwtAuthenticationConverter =
            new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

    return jwtAuthenticationConverter;
}
```

และผูกกับ resource server:

```java
.oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
)
```

## ทดสอบ 3 กรณี

ไม่ใส่ token:

```http
GET /api/v1/admin/dashboard
```

ควรได้:

```text
401 Unauthorized
```

ใช้ token ของ user ทั่วไป:

```http
GET /api/v1/admin/dashboard
Authorization: Bearer <USER_TOKEN>
```

ควรได้:

```text
403 Forbidden
```

ใช้ token ของ admin:

```http
GET /api/v1/admin/dashboard
Authorization: Bearer <ADMIN_TOKEN>
```

ควรได้:

```json
{
  "success": true,
  "message": "Admin dashboard",
  "data": "Only ADMIN can access this endpoint"
}
```

## 401 กับ 403 ต่างกันอย่างไร

| Status | ความหมาย |
| --- | --- |
| `401 Unauthorized` | ยังไม่ได้ยืนยันตัวตน หรือ token ใช้ไม่ได้ |
| `403 Forbidden` | ยืนยันตัวตนแล้ว แต่สิทธิ์ไม่พอ |

ถ้าใช้ token ของ `USER` แล้วได้ `401` แปลว่า token อาจผิดหรือหมดอายุ

ถ้าใช้ token ของ `USER` แล้วได้ `403` แปลว่า authentication ผ่านแล้ว แต่ role ไม่ใช่ `ADMIN`

## แบบฝึกหัดท้ายบท

1. สร้าง `AdminController`
2. ตั้ง rule `/api/v1/admin/**` เป็น `hasRole("ADMIN")`
3. ทดสอบไม่ใส่ token
4. ทดสอบ token ของ `USER`
5. แก้ user คนหนึ่งใน database เป็น `ADMIN`
6. login ใหม่แล้วทดสอบด้วย admin token

