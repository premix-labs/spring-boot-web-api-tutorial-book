---
title: โครงสร้าง Final Project
description: ภาพรวมไฟล์และโฟลเดอร์ของโปรเจกต์ Spring Boot สุดท้าย
---

# โครงสร้าง Final Project

Final project อยู่ที่โฟลเดอร์:

```text
examples/final-backend-api
```

โครงสร้างหลัก:

```text
final-backend-api/
  build.gradle
  settings.gradle
  gradlew
  gradlew.bat
  Dockerfile
  docker-compose.yml
  .env.example
  requests.http
  postman/
  src/
    main/
      java/com/example/backendapi/
        BackendApiApplication.java
        config/
        controller/
        dto/
        entity/
        exception/
        repository/
        security/
        service/
      resources/
        application.properties
        application-dev.properties
        application-prod.properties
        db/migration/
    test/
      java/com/example/backendapi/
      resources/application-test.properties
  README.md
```

หน้าที่ของแต่ละส่วน:

- `controller` รับ HTTP request และส่ง response
- `service` เก็บ business logic
- `repository` ติดต่อ database ผ่าน Spring Data JPA
- `entity` map กับ database table
- `dto` กำหนด request/response contract
- `security` เก็บ JWT, auth และ authorization behavior
- `exception` เก็บ global error handling
- `db/migration` เก็บ Flyway migration
- `postman` เก็บ collection สำหรับทดสอบ final project
- `.env.example` บอก environment variables โดยไม่เก็บ secret จริง

คำสั่งตรวจ:

```powershell
cd examples/final-backend-api
.\gradlew.bat clean test bootJar
docker compose config
```
