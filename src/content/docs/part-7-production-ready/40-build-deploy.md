---
title: 40 - Build และ Deploy
description: build jar, ตรวจ checklist และเตรียม deploy backend API
---

## เป้าหมายของบท

บทสุดท้ายจะสรุปขั้นตอน build และเตรียม deploy โปรเจกต์ `Backend API`

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- build jar อย่างไร
- run jar ด้วย profile production อย่างไร
- checklist ก่อน deploy มีอะไรบ้าง
- health check สำคัญอย่างไร
- roadmap หลังจบเล่มควรไปต่อเรื่องไหน

## Build application

ใช้คำสั่ง:

```powershell
.\gradlew.bat clean build
```

ถ้าใช้ macOS/Linux:

```bash
./gradlew clean build
```

artifact จะอยู่ที่:

```text
build/libs/
```

ตัวอย่าง:

```text
backend-api-0.0.1-SNAPSHOT.jar
```

## Run jar

PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://localhost:5432/secure_admin"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="admin123"
$env:JWT_SECRET="0123456789012345678901234567890123456789012345678901234567890123"

java -jar build/libs/backend-api-0.0.1-SNAPSHOT.jar
```

ถ้า deploy ด้วย platform เช่น Render, Railway, Fly.io, AWS, Azure หรือ GCP ให้ตั้งค่า environment variables ใน dashboard ของ platform นั้น

## เพิ่ม health endpoint

เพิ่ม dependency:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

ตั้งค่า:

```properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.probes.enabled=true
```

ตรวจ:

```text
GET /actuator/health
```

response:

```json
{
  "status": "UP"
}
```

health endpoint ช่วยให้ load balancer หรือ platform รู้ว่า application พร้อมรับ traffic หรือไม่

## Checklist ก่อน deploy

ตรวจสิ่งเหล่านี้:

- `./gradlew clean build` ผ่าน
- test ผ่าน
- profile production ใช้ env vars
- ไม่มี password/secret ใน Git
- Flyway migration ผ่าน
- `ddl-auto=validate`
- JWT secret ยาวพอและเปลี่ยนจากค่า demo
- log ไม่มี password/token เต็ม
- `/actuator/health` ใช้งานได้
- database backup/restore มีแผน
- admin endpoint ต้องใช้ role `ADMIN`
- Swagger UI ถูกเปิดหรือปิดตาม policy

## Checklist feature ก่อนจบเล่ม

ระบบควรทำได้:

- register user
- hash password
- login และออก JWT
- protect endpoint ด้วย JWT
- `/me` อ่าน current user
- `USER` เข้า admin endpoint ไม่ได้
- `ADMIN` ดู user list ได้
- `ADMIN` เปลี่ยน role/status ได้
- audit log ถูกบันทึก
- validation error อ่านง่าย
- exception response สม่ำเสมอ
- pagination ทำงาน
- build และ run ด้วย Docker Compose ได้

## Deploy flow โดยรวม

```text
1. Push code to GitHub
2. CI run test/build
3. Build Docker image หรือ jar
4. Deploy ไป platform
5. Set environment variables
6. Run Flyway migration
7. Health check ผ่าน
8. เปิด traffic
```

ช่วงแรกอาจ deploy manual ได้ แต่เมื่อโปรเจกต์จริงจังควรใช้ CI/CD

## Roadmap หลังจบหนังสือ

หัวข้อที่ควรเรียนต่อ:

- Refresh token
- Logout และ token revocation
- OAuth2/OIDC กับ provider จริง
- Redis caching
- Rate limiting
- CI/CD
- Observability ด้วย metrics/tracing
- Testcontainers
- Blue/green deployment
- Microservices

## ปิดเล่ม

ถ้าทำตามหนังสือจนจบ ผู้อ่านจะผ่าน flow หลักของ backend งานจริงแล้ว:

```text
REST API -> Database -> Better API -> Auth/JWT -> Admin -> Production Ready
```

จากจุดนี้สิ่งสำคัญคือสร้างโปรเจกต์ซ้ำด้วยตัวเองอีกครั้งโดยไม่ดูเฉลยทั้งหมด เพื่อให้เข้าใจ structure และ decision ของ Spring Boot มากขึ้น

