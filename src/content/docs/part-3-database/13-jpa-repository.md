---
title: 13 - JpaRepository
description: ใช้ Spring Data JPA ติดต่อฐานข้อมูลโดยไม่ต้องเขียน SQL พื้นฐาน
---

## เป้าหมายของบท

บทนี้จะสร้าง `UserRepository` เพื่อให้ service ติดต่อ database ผ่าน Spring Data JPA

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- Repository layer มีหน้าที่อะไร
- `JpaRepository<User, Long>` หมายความว่าอะไร
- method พื้นฐานที่ Spring Data JPA ให้มา
- การเขียน query จากชื่อ method เช่น `findByEmail`
- ทำไม method ที่หา record เดียวควร return `Optional<User>`

## Repository layer คืออะไร

Repository คือ layer ที่รับผิดชอบการคุยกับ database

ในระบบที่แยก layer ชัดเจน flow จะเป็น:

```text
Controller -> Service -> Repository -> Database
```

Controller ไม่ควรรู้ว่า database เป็น PostgreSQL หรือ MySQL

Service ควรรู้ว่าจะทำ business flow อะไร

Repository ควรรู้ว่าจะอ่านหรือเขียนข้อมูลอย่างไร

## สร้าง UserRepository

สร้างไฟล์:

```text
src/main/java/com/example/secureadmin/repository/UserRepository.java
```

```java
package com.example.secureadmin.repository;

import com.example.secureadmin.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
```

ไม่ต้องเขียน implementation เอง เพราะ Spring Data JPA จะสร้าง implementation ให้ตอน application start

## JpaRepository<User, Long> แปลว่าอะไร

```java
JpaRepository<User, Long>
```

แปลว่า repository นี้ทำงานกับ:

| ส่วน | ความหมาย |
| --- | --- |
| `User` | entity ที่ repository จัดการ |
| `Long` | type ของ primary key หรือ `id` |

เพราะใน `User` entity เราประกาศ:

```java
@Id
private Long id;
```

ดังนั้น generic ตัวที่สองจึงเป็น `Long`

## Method ที่ได้ฟรี

เมื่อ extends `JpaRepository` เราจะได้ method พื้นฐานจำนวนมาก เช่น:

| Method | ใช้ทำอะไร |
| --- | --- |
| `findAll()` | ดึงข้อมูลทั้งหมด |
| `findById(id)` | ดึงข้อมูลตาม id |
| `save(entity)` | insert หรือ update |
| `delete(entity)` | ลบ entity |
| `deleteById(id)` | ลบตาม id |
| `existsById(id)` | เช็กว่ามี id นี้ไหม |
| `count()` | นับจำนวน record |

นี่คือเหตุผลที่ Spring Data JPA ทำให้ CRUD พื้นฐานเร็วมาก

## Query จากชื่อ method

Spring Data JPA อ่านชื่อ method แล้วสร้าง query ให้

ตัวอย่าง:

```java
Optional<User> findByEmail(String email);
```

Spring จะตีความว่า:

```sql
select * from users where email = ?
```

ตัวอย่างอื่น:

```java
boolean existsByUsername(String username);
```

Spring จะตีความว่า:

```sql
select count(*) > 0 from users where username = ?
```

## ทำไมใช้ Optional

การค้นหาด้วย `id`, `email` หรือ `username` อาจไม่เจอข้อมูล

ดังนั้น method เหล่านี้ควร return `Optional<User>`:

```java
Optional<User> findByEmail(String email);
```

เวลาใช้ใน service จะเขียนได้ชัดเจน:

```java
User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
```

ความหมายคือ ถ้าเจอ user ให้ใช้ user นั้น ถ้าไม่เจอให้โยน error

## Repository ไม่ควรใส่ business logic

Repository ควรทำงานกับ database เท่านั้น เช่น:

- หา user จาก email
- บันทึก user
- ลบ user
- เช็กว่า username ซ้ำไหม

ส่วน logic แบบนี้ควรอยู่ใน service:

- สมัครสมาชิกได้หรือไม่
- email ซ้ำควรตอบ error อะไร
- admin เปลี่ยน role ได้หรือไม่
- user ที่ INACTIVE ห้าม login หรือไม่

การแยกแบบนี้ทำให้ code โตขึ้นแล้วไม่ปนกัน

## Checkpoint

หลังสร้าง repository แล้ว application ควร start ได้โดยไม่มี error

ถ้าเห็น error ประมาณนี้:

```text
Not a managed type: class com.example.secureadmin.model.User
```

มักหมายความว่า `User` ไม่ได้เป็น `@Entity` หรืออยู่ผิด package จน Spring Boot scan ไม่เจอ

## แบบฝึกหัดท้ายบท

1. สร้าง `UserRepository`
2. เพิ่ม method `findByEmail`
3. เพิ่ม method `existsByUsername`
4. รัน application ให้ start สำเร็จ
5. ลองตั้งชื่อ method ผิด เช่น `findByEmailAddress` แล้วดู error จาก Spring Data JPA
