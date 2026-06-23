# Examples

โฟลเดอร์นี้ใช้เก็บโค้ดตัวอย่างประกอบหนังสือ Spring Boot Tutorial Book

## โครงสร้าง

```text
examples/
  chapter-05-first-controller/
  chapter-07-user-api-in-memory/
  chapter-08-layered-architecture/
  chapter-09-postman-testing/
  chapter-11-postgresql-config/
  chapter-12-jpa-entity/
  chapter-13-jpa-repository/
  chapter-14-user-crud-database/
  chapter-15-update-user/
  chapter-16-validation/
  chapter-17-exception-handling/
  chapter-18-dto/
  chapter-19-response-format/
  chapter-20-pagination-sorting-filtering/
  chapter-21-register-design/
  chapter-22-password-hashing/
  chapter-23-login/
  chapter-24-jwt-token/
  chapter-25-current-user/
  chapter-26-protect-endpoints/
  chapter-27-roles/
  chapter-28-admin-only-endpoints/
  chapter-29-admin-user-list/
  chapter-30-change-role-status/
  chapter-31-audit-log/
  chapter-32-logging/
  chapter-33-transaction/
  chapter-34-flyway/
  chapter-35-profiles-env/
  chapter-36-openapi-swagger/
  chapter-37-unit-test/
  chapter-38-integration-test/
  chapter-39-docker-compose/
  chapter-40-build-deploy/
  final-backend-api/
```

## ตัวอย่างที่มีแล้ว

- `chapter-05-first-controller/`: controller แรก พร้อม endpoint `/hello`, `/health`, `/version`
- `chapter-07-user-api-in-memory/`: User API แบบใช้ `List<User>` ยังไม่ต่อ database
- `chapter-08-layered-architecture/`: แยก `UserService` ออกจาก `UserController`
- `chapter-09-postman-testing/`: ชุด request สำหรับทดสอบ API ด้วย Postman หรือ IntelliJ HTTP Client
- `chapter-11-postgresql-config/`: dependency, datasource config และ SQL สำหรับสร้าง database
- `chapter-12-jpa-entity/`: `User` entity พร้อม `Role` และ `UserStatus`
- `chapter-13-jpa-repository/`: `UserRepository` สำหรับ Spring Data JPA
- `chapter-14-user-crud-database/`: CRUD API ที่ต่อ PostgreSQL จริง
- `chapter-15-update-user/`: update endpoint สำหรับแก้ไข username/email
- `chapter-16-validation/`: DTO พร้อม Bean Validation และตัวอย่าง `@Valid`
- `chapter-17-exception-handling/`: custom exception และ `GlobalExceptionHandler`
- `chapter-18-dto/`: แยก request/response DTO ออกจาก entity
- `chapter-19-response-format/`: `ApiResponse<T>` สำหรับ success response
- `chapter-20-pagination-sorting-filtering/`: pagination, sorting และ keyword search
- `chapter-21-register-design/`: endpoint register และ `AuthService`
- `chapter-22-password-hashing/`: `PasswordEncoder` และ BCrypt hash
- `chapter-23-login/`: endpoint login และ error กรณี credential ไม่ถูกต้อง
- `chapter-24-jwt-token/`: `JwtService`, `JwtEncoder`, `JwtDecoder` และ resource server config
- `chapter-25-current-user/`: endpoint `/api/v1/auth/me`
- `chapter-26-protect-endpoints/`: public/protected/admin route rules
- `chapter-27-roles/`: role/status และ JWT role claim
- `chapter-28-admin-only-endpoints/`: admin-only dashboard และ security rule
- `chapter-29-admin-user-list/`: admin user list พร้อม filter
- `chapter-30-change-role-status/`: เปลี่ยน role/status ด้วย admin endpoint
- `chapter-31-audit-log/`: audit log สำหรับ action สำคัญของ admin
- `chapter-32-logging/`: logger และ log level
- `chapter-33-transaction/`: `@Transactional` กับ admin operation
- `chapter-34-flyway/`: Flyway migration scripts
- `chapter-35-profiles-env/`: dev/prod profiles และ environment variables
- `chapter-36-openapi-swagger/`: OpenAPI config และ Swagger UI
- `chapter-37-unit-test/`: unit test ด้วย JUnit/Mockito
- `chapter-38-integration-test/`: integration test ด้วย MockMvc
- `chapter-39-docker-compose/`: Dockerfile และ Docker Compose
- `chapter-40-build-deploy/`: build commands และ production checklist
- `final-backend-api/`: โปรเจกต์รวมสุดท้ายที่นำทุกบทมาประกอบเป็น API เดียว พร้อม Postman collection

## แนวทางเก็บตัวอย่าง

แต่ละ chapter ควรมี:

- `README.md`: อธิบายว่าใช้กับบทไหน และทดสอบอย่างไร
- source snippet หรือไฟล์ Java ที่คัดลอกไปใช้ได้
- expected response สำหรับ Postman, browser หรือ command line

โปรเจกต์ `final-backend-api/` ใช้เป็นตัวอย่างหลักสำหรับตรวจว่าแนวคิดทั้งเล่มประกอบกันเป็นระบบจริงได้
