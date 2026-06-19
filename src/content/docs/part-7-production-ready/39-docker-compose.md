---
title: 39 - Docker และ Docker Compose
description: รัน Spring Boot API และ PostgreSQL ด้วย container ที่ setup ซ้ำได้
---

## เป้าหมายของบท

บทนี้จะเพิ่ม Dockerfile และ Docker Compose เพื่อรัน `SecureAdmin API` กับ PostgreSQL ใน environment ที่ทำซ้ำได้

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- Dockerfile ใช้ทำอะไร
- Docker Compose ใช้รันหลาย service อย่างไร
- ทำไมใน container ต้องใช้ host `postgres` ไม่ใช่ `localhost`
- ส่ง environment variables เข้า container อย่างไร
- ใช้ healthcheck และ depends_on อย่างไร

## สร้าง Dockerfile

สร้างไฟล์:

```text
Dockerfile
```

```dockerfile
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

ก่อน build image ต้อง build jar:

```powershell
.\gradlew.bat clean build
```

## สร้าง docker-compose.yml

```yaml
services:
  postgres:
    image: postgres:18
    container_name: secure-admin-postgres
    environment:
      POSTGRES_DB: secure_admin
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: admin123
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d secure_admin"]
      interval: 5s
      timeout: 5s
      retries: 10

  api:
    build: .
    container_name: secure-admin-api
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_URL: jdbc:postgresql://postgres:5432/secure_admin
      DB_USERNAME: postgres
      DB_PASSWORD: admin123
      JWT_SECRET: 0123456789012345678901234567890123456789012345678901234567890123
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy

volumes:
  postgres-data:
```

สำหรับ `postgres:18` ขึ้นไป ให้ mount volume ที่ `/var/lib/postgresql`
ไม่ใช่ `/var/lib/postgresql/data` เพราะ image รุ่นใหม่จัดเก็บ data directory แบบแยกตาม major version ภายใน path นี้
ถ้าใช้ path เก่า container อาจ start ไม่ขึ้นและแจ้งว่า data อยู่ใน unused mount/volume

## localhost ใน container

ในเครื่องเรา PostgreSQL อาจอยู่ที่:

```text
localhost:5432
```

แต่ใน Docker Compose service `api` ต้องต่อไปที่:

```text
postgres:5432
```

เพราะ `localhost` ภายใน container หมายถึง container ตัวเอง ไม่ใช่ service PostgreSQL

## คำสั่งใช้งาน

build jar:

```powershell
.\gradlew.bat clean build
```

รัน compose:

```powershell
docker compose up --build
```

หยุด:

```powershell
docker compose down
```

ลบ volume database:

```powershell
docker compose down -v
```

ใช้ `-v` ด้วยความระวัง เพราะข้อมูล PostgreSQL จะหาย

## ไม่ควร commit secret จริง

ตัวอย่างนี้ใส่ password เพื่อการเรียน แต่ในงานจริงควรใช้ `.env` หรือ secret manager

ตัวอย่าง `.env`:

```text
DB_PASSWORD=change-me
JWT_SECRET=change-me-to-long-random-secret
```

แล้ว compose อ่าน:

```yaml
DB_PASSWORD: ${DB_PASSWORD}
JWT_SECRET: ${JWT_SECRET}
```

## แบบฝึกหัดท้ายบท

1. สร้าง `Dockerfile`
2. สร้าง `docker-compose.yml`
3. build jar
4. รัน `docker compose up --build`
5. เรียก `/swagger-ui/index.html`
6. ลองเปลี่ยน `DB_URL` เป็น `localhost` แล้วดู error จากนั้นเปลี่ยนกลับเป็น `postgres`
