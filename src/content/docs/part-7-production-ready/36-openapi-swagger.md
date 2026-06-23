---
title: 36 - OpenAPI และ Swagger
description: สร้างเอกสาร API ที่ frontend, tester และ backend ใช้ร่วมกันได้
---

## เป้าหมายของบท

บทนี้จะเพิ่ม OpenAPI document และ Swagger UI ให้โปรเจกต์ เพื่อให้ทีมเห็น contract ของ API ตรงกันและทดลองเรียก API ได้จาก browser

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- OpenAPI คืออะไร
- Swagger UI ช่วยอะไร
- เพิ่ม dependency springdoc อย่างไร
- ใส่ metadata API อย่างไร
- ใส่ JWT bearer security ใน Swagger อย่างไร

## ทำไมต้องมี OpenAPI

ถ้าไม่มี API documentation คนในทีมต้อง:

- อ่าน source code เอง
- ถาม backend ทุกครั้ง
- เดา request/response จาก Postman
- เจอปัญหา frontend กับ backend เข้าใจ contract ไม่ตรงกัน

OpenAPI ช่วยให้ contract ชัดขึ้น เช่น endpoint, method, request body, response schema และ security

## เพิ่ม dependency

สำหรับ Spring Boot 4 ให้ใช้ springdoc-openapi 3.x:

```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3'
```

ถ้าใช้ Spring Boot 3 ให้ตรวจ version springdoc 2.x ที่เข้ากับโปรเจกต์ของคุณ

หลังรัน application เปิด:

```text
http://localhost:8080/swagger-ui/index.html
```

และ OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## ตั้งค่า metadata

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/config/OpenApiConfig.java
```

```java
package com.example.backendapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI secureAdminOpenAPI() {
        String schemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Backend API")
                        .version("1.0.0")
                        .description("Spring Boot Backend API"))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
```

## ใส่ annotation ใน controller

ตัวอย่าง `AuthController`:

```java
@Operation(summary = "Register new user")
@PostMapping("/register")
public ApiResponse<UserResponse> register(
        @Valid @RequestBody RegisterRequest request
) {
    return ApiResponse.ok("User registered", authService.register(request));
}
```

import:

```java
import io.swagger.v3.oas.annotations.Operation;
```

## Swagger กับ JWT

เมื่อเพิ่ม bearer security แล้ว Swagger UI จะมีปุ่ม Authorize

ขั้นตอนทดสอบ:

1. เรียก `/api/v1/auth/login`
2. copy token จาก response
3. กด Authorize
4. ใส่ token แบบไม่ต้องมีคำว่า `Bearer`
5. ทดลองเรียก `/api/v1/auth/me` หรือ admin endpoint

## ควรเปิด Swagger ใน production ไหม

ขึ้นกับระบบ

ถ้าเปิดใน production ต้องควบคุมการเข้าถึง เช่น:

- เปิดเฉพาะ internal network
- เปิดเฉพาะ environment non-prod
- protect ด้วย authentication

สำหรับมือใหม่ แนะนำเปิดใน dev/staging ก่อน และค่อยตัดสินใจ production ตาม policy ของทีม

## แบบฝึกหัดท้ายบท

1. เพิ่ม springdoc dependency
2. เปิด Swagger UI
3. เพิ่ม `OpenApiConfig`
4. ใส่ `@Operation` ให้ register/login
5. ทดลองใส่ JWT ใน Swagger UI แล้วเรียก `/me`

