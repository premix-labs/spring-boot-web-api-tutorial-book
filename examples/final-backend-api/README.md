# Final Backend API

โปรเจกต์นี้คือโค้ดตัวอย่างปลายทางของหนังสือ Spring Boot Tutorial Book

เป้าหมายคือรวมสิ่งที่สอนตลอดเล่มไว้ในโปรเจกต์เดียว:

- REST API ด้วย Spring Boot
- Layered Architecture: Controller, Service, Repository, Model
- PostgreSQL + Spring Data JPA
- Flyway migration
- Validation และ Global Exception Handler
- Register / Login
- Password hashing ด้วย BCrypt
- JWT authentication
- Role-based access control ด้วย `USER` และ `ADMIN`
- Admin user management
- Audit log
- OpenAPI / Swagger UI
- Unit test และ integration test
- Docker Compose สำหรับรัน API กับ PostgreSQL

## โครงสร้างหลัก

```text
src/main/java/com/example/backendapi/
  bootstrap/    สร้าง admin เริ่มต้นตาม config
  common/       response wrapper และ page response
  config/       security, JWT, OpenAPI, properties
  controller/   REST controllers
  dto/          request/response DTO
  exception/    custom exception และ global handler
  model/        JPA entities และ enum
  repository/   Spring Data JPA repositories
  service/      business logic
```

## รัน test

โปรเจกต์นี้ใช้ JDK 25 ตามตัวอย่างในหนังสือ

```powershell
.\gradlew.bat test
```

test profile ใช้ H2 in-memory และให้ Hibernate สร้าง schema ชั่วคราวด้วย `create-drop` เพื่อให้ test รันง่าย ส่วน dev/prod profile ยังใช้ Flyway migration กับ PostgreSQL

ถ้าเครื่องยังไม่ได้ตั้ง `JAVA_HOME` ให้ตั้งไปที่ JDK ก่อน เช่น:

```powershell
$env:JAVA_HOME="C:\path\to\jdk-25"
.\gradlew.bat test
```

## รันด้วย PostgreSQL ในเครื่อง

สร้าง database:

```sql
CREATE DATABASE secure_admin;
CREATE USER secure_admin WITH PASSWORD 'secure_admin';
GRANT ALL PRIVILEGES ON DATABASE secure_admin TO secure_admin;
```

จากนั้นรันแอปด้วย profile `dev`

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\gradlew.bat bootRun
```

ค่าเริ่มต้นของ dev profile:

- database: `secure_admin`
- username: `secure_admin`
- password: `secure_admin`
- bootstrap admin email: `admin@example.com`
- bootstrap admin password: `admin12345`

## รันด้วย Docker Compose

build jar ก่อน:

```powershell
$env:JAVA_HOME="C:\path\to\jdk-25"
.\gradlew.bat clean bootJar
```

จากนั้นรัน Docker Compose:

```powershell
Copy-Item .env.example .env
docker compose up --build
```

## Endpoint สำคัญ

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/me`
- `GET /api/v1/admin/users`
- `GET /api/v1/admin/users/{id}`
- `PATCH /api/v1/admin/users/{id}/role`
- `PATCH /api/v1/admin/users/{id}/status`
- `GET /api/v1/admin/audit-logs`
- `GET /swagger-ui.html`
- `GET /actuator/health`

ตัวอย่าง request อยู่ใน `requests.http`

## Postman collection

สามารถ import ไฟล์เหล่านี้เข้า Postman ได้:

```text
postman/backend-api.postman_collection.json
postman/backend-api-local.postman_environment.json
```

ลำดับการทดสอบที่แนะนำ:

1. `Health Check`
2. `Register Normal User`
3. `Login Bootstrap Admin`
4. `Get Current User`
5. `List Users`
6. `Change Registered User Role To ADMIN`
7. `Change Registered User Status To INACTIVE`
8. `List Audit Logs`

## หมายเหตุด้าน production

ค่าต่อไปนี้ห้ามใช้ค่า default ใน production:

- `JWT_SECRET`
- `BOOTSTRAP_ADMIN_PASSWORD`
- database username/password

ใน production ควรตั้งผ่าน environment variables และปิด bootstrap admin หลังสร้าง admin คนแรกแล้ว
