---
title: 12 - JPA และ Entity
description: สร้าง Entity เพื่อ map Java class กับ table ใน PostgreSQL
---

## เป้าหมายของบท

บทนี้จะสร้าง `User` entity ซึ่งเป็น class Java ที่ JPA ใช้แทน table ใน database

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- `@Entity` คืออะไร
- `@Table`, `@Id`, `@GeneratedValue`, `@Column` ใช้ทำอะไร
- enum ควรเก็บลง database อย่างไร
- ทำไมไม่ควรส่ง entity ออกเป็น response ตรง ๆ ในงานจริง
- ทำไมไม่ควรตั้งชื่อตารางว่า `user`

## Entity คืออะไร

Entity คือ class ที่ JPA/Hibernate ใช้ map กับ table ใน database

ตัวอย่างแนวคิด:

```text
Java class: User
Database table: users
Object: new User(...)
Row: 1 แถวใน table users
```

เมื่อเราเรียก `userRepository.save(user)` Hibernate จะเปลี่ยน object เป็น SQL เพื่อ insert หรือ update table ให้

## สร้าง enum สำหรับ role

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/model/Role.java
```

```java
package com.example.secureadmin.model;

public enum Role {
    USER,
    ADMIN
}
```

ตอนนี้มีแค่ `USER` และ `ADMIN` เพราะหนังสือจะใช้ role นี้ไปจนถึงระบบ admin

## สร้าง enum สำหรับ status

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/model/UserStatus.java
```

```java
package com.example.secureadmin.model;

public enum UserStatus {
    ACTIVE,
    INACTIVE
}
```

`ACTIVE` หมายถึงผู้ใช้ใช้งานได้ตามปกติ

`INACTIVE` หมายถึงผู้ใช้ถูกปิดการใช้งาน เช่น admin สั่งระงับบัญชี

## สร้าง User entity

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/model/User.java
```

```java
package com.example.secureadmin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

## อธิบาย annotation สำคัญ

| Annotation | หน้าที่ |
| --- | --- |
| `@Entity` | บอก JPA ว่า class นี้เป็น entity |
| `@Table(name = "users")` | กำหนดชื่อตารางใน database |
| `@Id` | กำหนด primary key |
| `@GeneratedValue` | ให้ database สร้างค่า id อัตโนมัติ |
| `@Column(nullable = false)` | column นี้ห้ามเป็น `NULL` |
| `@Column(unique = true)` | ห้ามมีค่าซ้ำ |
| `@Enumerated(EnumType.STRING)` | เก็บ enum เป็น string เช่น `USER`, `ADMIN` |
| `@PrePersist` | ทำงานก่อน insert ครั้งแรก |
| `@PreUpdate` | ทำงานก่อน update |

## ทำไมใช้ EnumType.STRING

ถ้าไม่กำหนดให้ชัด บางกรณี enum อาจถูกเก็บเป็นเลข เช่น `0`, `1`

การเก็บเป็นเลขอ่านยากและเสี่ยง ถ้าวันหนึ่งเราเพิ่ม enum ใหม่หรือสลับลำดับ enum ข้อมูลเดิมอาจตีความผิด

ดังนั้นในระบบจริงควรใช้:

```java
@Enumerated(EnumType.STRING)
```

เพื่อให้ database เก็บค่าเป็นข้อความ เช่น:

```text
USER
ADMIN
ACTIVE
INACTIVE
```

## ทำไมไม่ใช้ชื่อตาราง user

ใน PostgreSQL คำว่า `user` อาจชนกับ keyword หรือ object ภายใน database

ใช้ `users` จะชัดและปลอดภัยกว่า:

```java
@Table(name = "users")
```

## ข้อควรระวังเรื่อง password

ในบทนี้ field `password` ยังเป็น string ปกติเพื่อให้เข้าใจ database flow ก่อน

ในงานจริงห้ามเก็บ password แบบ plain text เด็ดขาด เราจะกลับมาแก้ให้เป็น password hash ในบท `22 - เข้ารหัสรหัสผ่าน`

และไม่ควรส่ง `password` ออกไปใน response ไม่ว่าจะเป็น plain text หรือ hash

## ตรวจ table ที่ถูกสร้าง

หลังรัน application แล้ว ถ้าใช้ `ddl-auto=update` และ entity ถูก scan สำเร็จ Hibernate จะสร้าง table `users`

ตัวอย่าง column ที่ควรเห็น:

```text
id
username
email
password
role
status
created_at
updated_at
```

ชื่อ column อาจถูกแปลงจาก `createdAt` เป็น `created_at` ตาม naming strategy ของ Hibernate

## แบบฝึกหัดท้ายบท

1. สร้าง `Role` enum
2. สร้าง `UserStatus` enum
3. สร้าง `User` entity
4. รัน application แล้วตรวจว่า table `users` ถูกสร้าง
5. ลองลบ `@Table(name = "users")` แล้วดูว่า Hibernate ตั้งชื่อตารางอย่างไร จากนั้นใส่กลับตามเดิม

