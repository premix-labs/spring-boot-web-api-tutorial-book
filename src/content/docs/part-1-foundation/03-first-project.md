---
title: 03 - สร้างโปรเจกต์แรก
description: สร้าง Spring Boot project ด้วย Spring Initializr
---

## เป้าหมายของบท

บทนี้จะสร้าง Spring Boot project แรกชื่อ `backend-api` แล้วรันให้ application เริ่มทำงานสำเร็จ

## สร้าง project ด้วย Spring Initializr

เปิด Spring Initializr แล้วเลือกค่าหลัก:

```text
Project: Gradle
Language: Java
Spring Boot: 4.x
Group: com.example
Artifact: backend-api
Name: backend-api
Package name: com.example.backendapi
Packaging: Jar
Java: 25
```

## Dependency ที่เลือก

เลือก dependency เหล่านี้:

- Spring Web MVC
- Validation
- Lombok

เหตุผล:

- `Spring Web MVC` ใช้สร้าง REST API
- `Validation` ใช้ตรวจ request
- `Lombok` ลดโค้ด getter/setter/constructor

ยังไม่ต้องเลือก `Spring Data JPA` และ `PostgreSQL Driver` ในบทนี้ เพราะเรายังไม่ได้ตั้งค่า database ถ้าเพิ่ม dependency สองตัวนี้เร็วเกินไป Spring Boot จะพยายามสร้าง datasource ทันที และ application อาจ start ไม่ขึ้นด้วย error เรื่อง database config

เราจะค่อยเพิ่ม `Spring Data JPA` และ `PostgreSQL Driver` ในบทที่ 11 ตอนเริ่มเชื่อม PostgreSQL จริง

## เปิด project ใน IntelliJ

หลัง download project แล้ว unzip จากนั้นเปิด folder ด้วย IntelliJ IDEA

สิ่งที่ควรเห็น:

```text
backend-api/
  build.gradle
  settings.gradle
  src/
    main/
      java/
      resources/
    test/
```

## ไฟล์สำคัญ

`build.gradle` ใช้จัดการ dependency และ plugin ของ Gradle

`settings.gradle` เก็บชื่อ root project

`DemoApplication.java` หรือ `BackendApiApplication.java` คือ entry point ของ Spring Boot

`application.properties` ใช้เก็บ configuration เช่น application name, database URL, port

## Entry point ของ Spring Boot

ตัวอย่าง:

```java
package com.example.backendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApiApplication.class, args);
    }
}
```

`@SpringBootApplication` ทำให้ Spring Boot เริ่ม scan component และ auto configuration ต่าง ๆ

## รัน application

บน Windows:

```powershell
.\gradlew.bat bootRun
```

บน macOS/Linux:

```powershell
./gradlew bootRun
```

ถ้าสำเร็จจะเห็น log ประมาณนี้:

```text
Tomcat started on port 8080
Started BackendApiApplication
```

## ตั้งชื่อ application

เปิด `src/main/resources/application.properties`

```properties
spring.application.name=backend-api
```

## ปัญหาที่พบบ่อย

### Build failed เพราะ Java version ไม่ตรง

ตรวจ `java -version` และค่า Java ใน `build.gradle`

### Lombok error ใน IDE

ให้เปิด annotation processing ใน IntelliJ และตรวจว่ามี Lombok plugin

### Application รันแล้วปิดทันที

ตรวจว่ามี dependency `Spring Web MVC` หรือไม่ ถ้าไม่มี web server อาจไม่ถูกเปิด

## Checkpoint

บทนี้ถือว่าสำเร็จเมื่อ:

- เปิด project ใน IntelliJ ได้
- รัน `bootRun` ผ่าน
- เห็น Spring Boot started ใน console
- มี `application.properties` พร้อมใช้งาน

## แบบฝึกหัดท้ายบท

เปลี่ยนชื่อ application เป็นชื่อของตัวเอง แล้วรัน project อีกครั้ง
