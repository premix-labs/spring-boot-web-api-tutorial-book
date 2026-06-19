---
title: 38 - Integration Test
description: ทดสอบ API flow กับ Spring context จริงด้วย MockMvc
---

## เป้าหมายของบท

บทนี้จะเขียน integration test สำหรับ controller/service/repository ทำงานร่วมกัน

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- integration test ต่างจาก unit test อย่างไร
- ใช้ `@SpringBootTest` และ `@AutoConfigureMockMvc`
- ทดสอบ register/login ผ่าน HTTP layer อย่างไร
- ใช้ profile test แยกจาก dev/prod อย่างไร
- ทำไม test database ควรแยกจาก dev database

## Integration test คืออะไร

Integration test ทดสอบหลาย layer พร้อมกัน เช่น:

```text
MockMvc -> Controller -> Service -> Repository -> Test database
```

ช้ากว่า unit test แต่มั่นใจกว่าในเรื่อง wiring, validation, security และ JSON mapping

## สร้าง application-test.properties

```properties
spring.datasource.url=jdbc:h2:mem:secure_admin_test
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false

app.jwt.secret=0123456789012345678901234567890123456789012345678901234567890123
app.jwt.expiration-seconds=86400
app.jwt.issuer=secure-admin-api-test
```

ถ้าไม่ใช้ H2 สามารถใช้ Testcontainers กับ PostgreSQL ได้ ซึ่งใกล้ production กว่า แต่ซับซ้อนกว่าเล็กน้อย

## เพิ่ม dependency test database

```groovy
testRuntimeOnly 'com.h2database:h2'
```

## สร้าง AuthControllerIntegrationTest

```java
package com.example.secureadmin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void register_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "john",
                                  "email": "john@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void register_shouldReturnBadRequest_whenEmailInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "john",
                                  "email": "wrong",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
```

## ทดสอบ login flow

ในการทดสอบ login สำเร็จ ต้อง register ก่อน แล้ว login:

```java
@Test
void login_shouldReturnToken() throws Exception {
    mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "username": "jane",
                      "email": "jane@example.com",
                      "password": "password123"
                    }
                    """));

    mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "email": "jane@example.com",
                              "password": "password123"
                            }
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.token").exists());
}
```

## ข้อควรระวัง

- test database ต้องแยกจาก dev database
- test ควร repeatable รันกี่ครั้งก็ผ่าน
- อย่าพึ่งข้อมูลที่มีอยู่ก่อนใน database จริง
- integration test ไม่ควรแทน unit test ทั้งหมด เพราะช้ากว่า

## แบบฝึกหัดท้ายบท

1. สร้าง `application-test.properties`
2. เพิ่ม H2 dependency สำหรับ test
3. เขียน test register success
4. เขียน test register validation error
5. เขียน test login success
6. เขียน test login wrong password

