---
title: 22 - Hash Password
description: เก็บ password อย่างปลอดภัยด้วย PasswordEncoder
---

## เป้าหมายของบท

บทนี้จะแก้ register flow ให้ไม่เก็บ password เป็น plain text อีกต่อไป

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- password hashing ต่างจาก encryption อย่างไร
- ใช้ `PasswordEncoder` อย่างไร
- ทำไมต้องใช้ `matches()` ตอน login
- ทำไม hash ของ password เดียวกันอาจไม่เหมือนกัน
- ทำไมถึงไม่ควรเขียน algorithm hash เอง

## ห้ามเก็บ password เป็น plain text

ถ้า database มีข้อมูลแบบนี้ถือว่าอันตรายมาก:

```text
email             password
john@example.com password123
```

ถ้า database หลุด ผู้โจมตีจะได้ password จริงทันที และผู้ใช้จำนวนมากมักใช้ password ซ้ำกับระบบอื่น

สิ่งที่ควรเก็บคือ password hash:

```text
email             password
john@example.com $2a$10$...
```

hash ที่ดีไม่ควรถอดกลับเป็น password เดิมได้

## เพิ่ม Spring Security dependency

เปิด `build.gradle` แล้วเพิ่ม:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```

จากนั้น reload Gradle

## สร้าง SecurityBeansConfig

สร้างไฟล์:

```text
src/main/java/com/example/backendapi/config/SecurityBeansConfig.java
```

```java
package com.example.backendapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

`BCryptPasswordEncoder` เป็น encoder ที่เหมาะกับ password เพราะออกแบบมาให้คำนวณช้าพอสมควรและมี salt

## ปรับ AuthService

เพิ่ม `PasswordEncoder` เข้าไปใน service:

```java
private final PasswordEncoder passwordEncoder;
```

แล้วเปลี่ยนตอน register:

```java
String hashedPassword = passwordEncoder.encode(request.password());
user.setPassword(hashedPassword);
```

ตัวอย่างเต็ม:

```java
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUserException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return toResponse(userRepository.save(user));
    }
}
```

## ตรวจ password ใน database

หลัง register แล้ว ให้เปิด database ดู column `password`

ค่าที่ได้ควรหน้าตาประมาณนี้:

```text
$2a$10$K1Qp...
```

ถ้าเห็น `password123` แปลว่ายังไม่ได้ hash หรือ service ยังไม่ได้ใช้ version ใหม่

## ตอน login ต้องใช้ matches

ห้ามเทียบ password แบบนี้:

```java
request.password().equals(user.getPassword())
```

เพราะ `user.getPassword()` คือ hash ไม่ใช่ password จริง

ต้องใช้:

```java
passwordEncoder.matches(request.password(), user.getPassword())
```

`matches()` จะนำ password ที่ user ส่งมาไปตรวจเทียบกับ hash เดิมอย่างถูกวิธี

## ทำไม password เดียวกัน hash ไม่เหมือนกัน

BCrypt ใส่ salt เข้าไปด้วย ทำให้ password เดียวกันอาจได้ hash ไม่เหมือนกัน

นี่เป็นเรื่องดี เพราะทำให้ผู้โจมตีเดายากขึ้นว่าผู้ใช้หลายคนใช้ password เดียวกันหรือไม่

## แบบฝึกหัดท้ายบท

1. เพิ่ม `spring-boot-starter-security`
2. สร้าง `PasswordEncoder` bean
3. แก้ register ให้ hash password
4. register user 2 คนด้วย password เดียวกัน
5. ตรวจว่า hash ใน database ไม่ใช่ plain text

