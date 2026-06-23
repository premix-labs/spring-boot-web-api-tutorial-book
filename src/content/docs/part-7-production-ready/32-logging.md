---
title: 32 - Logging
description: ใช้ logger, log level และแนวทาง log ที่ปลอดภัยใน production
---

## เป้าหมายของบท

บทนี้จะเปลี่ยนจากการ debug ด้วย `System.out.println` ไปใช้ logging ที่ควบคุม level และอ่านปัญหา production ได้จริง

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- ทำไมไม่ควรใช้ `System.out.println` ในงานจริง
- ใช้ SLF4J logger อย่างไร
- เลือก `debug`, `info`, `warn`, `error` อย่างไร
- ข้อมูลอะไรห้าม log
- ตั้งค่า log level ใน `application.properties` อย่างไร

## ทำไมต้องใช้ logger

`System.out.println` ใช้ทดลองตอนเริ่มเรียนได้ แต่ไม่เหมาะกับ backend จริง เพราะ:

- ปิด/เปิดตาม level ไม่สะดวก
- ไม่มี format มาตรฐาน
- filter ตาม package ยาก
- ส่งต่อไป log platform ยาก
- เสี่ยง print ข้อมูลลับโดยไม่ตั้งใจ

Spring Boot ใช้ logging system มาให้แล้ว เราจึงควรใช้ logger ตั้งแต่ service/controller

## สร้าง logger

ตัวอย่างใน `AuthService`:

```java
package com.example.backendapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
}
```

ถ้าใช้ Lombok สามารถใช้:

```java
@Slf4j
@Service
public class AuthService {
}
```

ในหนังสือจะใช้ `LoggerFactory` เพราะมือใหม่เห็นที่มาชัดเจนกว่า

## Log level ที่ควรรู้

| Level | ใช้เมื่อไร |
| --- | --- |
| `debug` | รายละเอียดสำหรับ debug เช่น branch ที่ระบบเลือก |
| `info` | เหตุการณ์ปกติที่สำคัญ เช่น login สำเร็จ |
| `warn` | สิ่งผิดปกติแต่ระบบยังทำงานต่อได้ เช่น login failed |
| `error` | error ที่ควรตรวจ เช่น database operation ล้มเหลว |

## ตัวอย่างใน AuthService

```java
public LoginResponse login(LoginRequest request) {
    log.info("Login attempt email={}", request.email());

    User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> {
                log.warn("Login failed email={} reason=user_not_found", request.email());
                return new BadCredentialsException("Invalid email or password");
            });

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
        log.warn("Login failed email={} reason=bad_password", request.email());
        throw new BadCredentialsException("Invalid email or password");
    }

    log.info("Login successful userId={}", user.getId());
    return new LoginResponse(jwtService.generateToken(user), toResponse(user));
}
```

ใช้ `{}` แทนการต่อ string:

```java
log.info("Login successful userId={}", user.getId());
```

ดีกว่า:

```java
log.info("Login successful userId=" + user.getId());
```

เพราะ logger สามารถจัดการ parameter และ performance ได้ดีกว่า

## ห้าม log ข้อมูลลับ

ห้าม log:

- password
- password hash
- JWT token เต็มก้อน
- secret key
- database password
- personal data ที่ไม่จำเป็น

ตัวอย่างที่ไม่ควรทำ:

```java
log.info("Login request password={}", request.password());
log.info("JWT token={}", token);
```

ถ้าจำเป็นต้อง debug token ให้ log เฉพาะ metadata เช่น user id หรือ token id ไม่ใช่ token เต็ม

## ตั้งค่า log level

ใน `application-dev.properties`:

```properties
logging.level.com.example.backendapi=DEBUG
logging.level.org.springframework.security=INFO
```

ใน `application-prod.properties`:

```properties
logging.level.com.example.backendapi=INFO
logging.level.org.springframework.security=WARN
```

dev เปิด debug ได้ แต่ production ควรระวัง log volume และข้อมูล sensitive

## Log format ที่อ่านง่าย

ในช่วงเริ่มต้นใช้ default ของ Spring Boot ได้ก่อน สิ่งสำคัญคือ message ต้องค้นหาได้:

```text
Login successful userId=1
Admin changed user status actorId=10 targetUserId=12 old=ACTIVE new=INACTIVE
```

ข้อความ log ควรมี:

- action
- id สำคัญ
- result หรือ reason
- ไม่มีข้อมูลลับ

## แบบฝึกหัดท้ายบท

1. เพิ่ม logger ใน `AuthService`
2. log ตอน register สำเร็จ
3. log ตอน login สำเร็จ
4. log ตอน login failed ด้วย `warn`
5. ตรวจว่าไม่มี password หรือ token เต็มใน log

