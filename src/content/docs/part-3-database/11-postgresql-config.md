---
title: 11 - ตั้งค่า PostgreSQL
description: ตั้งค่า dependency, database และ datasource ให้ Spring Boot เชื่อม PostgreSQL ได้
---

## เป้าหมายของบท

บทนี้จะเปลี่ยน API จากการเก็บข้อมูลใน memory ไปสู่ database จริง โดยใช้ PostgreSQL เป็นฐานข้อมูลหลักของโปรเจกต์ `Backend API`

หลังจบบทนี้ผู้อ่านควรทำได้:

- เพิ่ม dependency สำหรับ Spring Data JPA และ PostgreSQL Driver
- สร้าง database ชื่อ `secure_admin`
- ตั้งค่า `application.properties`
- เข้าใจว่า `ddl-auto=update` ใช้เพื่อการเรียน ไม่ใช่แนวทาง production
- debug ปัญหาเชื่อมต่อ database เบื้องต้นได้

## ทำไมต้องใช้ database

ในบทก่อนหน้าเราใช้ `List<User>` เก็บข้อมูลใน memory ข้อดีคือเริ่มง่าย แต่ข้อเสียคือข้อมูลหายทันทีเมื่อ restart application

ระบบจริงต้องเก็บข้อมูลถาวร เช่น:

- user ที่ register แล้ว
- password hash
- role และ status ของผู้ใช้
- audit log
- token หรือ session metadata ในบางระบบ

ดังนั้นตั้งแต่บทนี้เป็นต้นไป flow จะเปลี่ยนเป็น:

```text
Client -> Controller -> Service -> Repository -> PostgreSQL
```

## เพิ่ม dependency

เปิดไฟล์ `build.gradle` แล้วตรวจว่ามี dependency เหล่านี้:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    runtimeOnly 'org.postgresql:postgresql'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

`spring-boot-starter-data-jpa` ทำให้เราใช้ JPA, Hibernate และ `JpaRepository` ได้

`postgresql` คือ driver ที่ทำให้ Java application ติดต่อ PostgreSQL ได้

ถ้าคุณสร้าง project ตามบทที่ 3 จะยังไม่มีสองบรรทัดนี้:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
runtimeOnly 'org.postgresql:postgresql'
```

ให้เพิ่มในบทนี้พร้อมตั้งค่า datasource ด้านล่าง ไม่ควรเพิ่ม JPA/PostgreSQL ตั้งแต่บทแรกโดยยังไม่มี database config

## สร้าง database

เปิด PostgreSQL client เช่น pgAdmin, DBeaver หรือ command line แล้วสร้าง database:

```sql
CREATE DATABASE secure_admin;
```

ในช่วงเรียนให้ใช้ database แยกสำหรับโปรเจกต์นี้ ไม่ควรใช้ database รวมกับโปรเจกต์อื่น เพราะ Hibernate อาจสร้างหรือแก้ไข table ระหว่างทดลอง

## ตั้งค่า application.properties

เปิดไฟล์:

```text
src/main/resources/application.properties
```

แล้วตั้งค่าดังนี้:

```properties
spring.application.name=backend-api

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/secure_admin}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:admin123}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

จุดที่ควรเข้าใจ:

| Config | ความหมาย |
| --- | --- |
| `spring.datasource.url` | ที่อยู่ของ PostgreSQL database |
| `spring.datasource.username` | username สำหรับเชื่อมต่อ database |
| `spring.datasource.password` | password สำหรับเชื่อมต่อ database |
| `spring.jpa.hibernate.ddl-auto=update` | ให้ Hibernate สร้างหรือปรับ table จาก entity ระหว่างเรียน |
| `spring.jpa.open-in-view=false` | ปิดการเปิด database session ยาวไปถึง view layer |
| `spring.jpa.show-sql=true` | แสดง SQL ใน log เพื่อช่วยเรียนและ debug |

รูปแบบ `${DB_URL:...}` หมายความว่า ถ้ามี environment variable ชื่อ `DB_URL` ให้ใช้ค่านั้น แต่ถ้าไม่มีให้ใช้ค่าหลังเครื่องหมาย `:` แทน

## ทดสอบการเชื่อมต่อ

หลังตั้งค่าเสร็จ ให้รัน application:

```powershell
./gradlew bootRun
```

ถ้าเชื่อมต่อสำเร็จ ใน log ควรเห็นข้อความเกี่ยวกับ Hibernate และ Hikari connection pool โดยไม่มี error เรื่อง password, database หรือ connection refused

ถ้า application ยังไม่มี entity อาจยังไม่เห็น table ถูกสร้าง ถือว่าไม่ผิด บทถัดไปจะสร้าง `User` entity แล้ว Hibernate จะเริ่มสร้าง table ให้

## เรื่องสำคัญของ ddl-auto

ในหนังสือช่วงเรียนจะใช้:

```properties
spring.jpa.hibernate.ddl-auto=update
```

เพราะช่วยลดขั้นตอนสำหรับมือใหม่ Hibernate จะดู entity แล้วสร้างหรือปรับ table ให้เอง

แต่ในงานจริงไม่ควรพึ่ง `update` ระยะยาว เพราะ schema อาจเปลี่ยนแบบควบคุมยาก เมื่อเข้าสู่ภาค production-ready เราจะเปลี่ยนไปใช้ Flyway เพื่อจัดการ migration อย่างเป็นระบบ

## ปัญหาที่พบบ่อย

| อาการ | สาเหตุที่เป็นไปได้ | วิธีตรวจ |
| --- | --- | --- |
| `Connection refused` | PostgreSQL ยังไม่เปิด หรือ port ไม่ใช่ `5432` | ตรวจ service PostgreSQL |
| `database "secure_admin" does not exist` | ยังไม่ได้สร้าง database | รัน `CREATE DATABASE secure_admin;` |
| `password authentication failed` | username/password ผิด | ตรวจค่าใน `application.properties` |
| `No suitable driver` | ไม่มี PostgreSQL Driver | ตรวจ `runtimeOnly 'org.postgresql:postgresql'` |
| ตารางไม่ถูกสร้าง | ยังไม่มี entity หรือ entity ไม่ถูก scan | ตรวจ package ของ class entity |

## แบบฝึกหัดท้ายบท

1. สร้าง database `secure_admin`
2. ตั้งค่า `application.properties`
3. รัน application ให้เชื่อมต่อ PostgreSQL สำเร็จ
4. ลองเปลี่ยน password ให้ผิด แล้วอ่านข้อความ error
5. กลับมาแก้ password ให้ถูก แล้วรันใหม่อีกครั้ง
