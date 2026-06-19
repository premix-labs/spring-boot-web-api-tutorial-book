# Spring Boot Tutorial Book Validation Report

## Update 2026-06-15: chapter-by-chapter validation continued

Latest status: publish-ready for GitHub Pages from the validation scope below.

Validated after the earlier report:

- Chapters 15-20: compiled and ran real HTTP flows for update user, validation, exception handling, DTO response, response wrapper, pagination/search. Passed.
- Chapters 21-26: compiled and ran register/login/JWT/current user/admin route protection. Passed.
- Chapters 27-31: compiled and ran admin user list, change role/status, and audit log creation. Passed.
- Chapters 32-40: checked production-ready material against the final project, then ran final project tests/build. Passed.
- Book site build: `npm run build` passed.
- Final project build: `examples/final-secure-admin-api/gradlew.bat clean test bootJar` passed.
- Docker Compose smoke test was completed after Docker Desktop was started. The final project now runs with `postgres:18`.

Issues found and fixed in this validation pass:

- Chapter 24 now tells readers to delete `SecurityBeansConfig.java` from chapter 22 before creating `SecurityConfig.java`, preventing duplicate `PasswordEncoder` bean errors.
- Chapter 29 `UserRepository` example now keeps the normal user-list search method from chapter 20 while adding admin search, preventing `UserService` compile errors.
- Chapter 31 now includes the updated `AdminUserController` that passes `authentication.getName()` to role/status changes, so audit logging compiles and records the actor.
- Remaining `disable own account` wording was changed to `deactivate own account` to match the `INACTIVE` status naming.
- Docker Compose was updated for PostgreSQL 18 by mounting the database volume at `/var/lib/postgresql` and waiting for PostgreSQL health before starting the API.
- Flyway dependency was updated to `spring-boot-starter-flyway` for Spring Boot 4 so migrations run before Hibernate schema validation.
- Admin user search was updated to pass an empty keyword instead of `null`, avoiding PostgreSQL/Hibernate 7 `lower(bytea)` errors.
- The final project now logs unexpected server errors on the server side while still hiding stack traces from API clients.

Runtime checks confirmed:

```text
Chapter 15-20: validation 400, duplicate 409, not found 404, password hidden, pagination OK
Chapter 21-26: JWT issued, /me requires token, USER admin access 403, ADMIN access OK
Chapter 27-31: admin list OK, role/status change OK, audit log count = 2
Final project: BUILD SUCCESSFUL
Astro book: build complete
Docker Compose postgres:18: health UP, login JWT OK, /me OK, admin users OK
```

วันที่ตรวจ: 2026-06-15

## สรุปผล

สถานะโดยรวม: หนังสือมีโครงสร้างและ final project ที่ build ได้จริง แต่ยังไม่ควรถือว่า publish-ready จนกว่าจะแก้ประเด็นลำดับ dependency และความสอดคล้องของ enum/status

สิ่งที่ตรวจแล้ว:

- สร้างโปรเจกต์ใหม่จาก Spring Initializr ตามบทที่ 3
- ใช้ JDK 25 จาก `C:\Users\den03.CIVILPARK.000\.jdks\temurin-25.0.3`
- ใช้ PostgreSQL local `postgres/admin123`
- ไล่ทำจริงถึง milestone บท 14
- รัน final project ด้วย Gradle wrapper จาก validation project

## ผลการรันจริง

### บท 3: สร้างโปรเจกต์แรก

ผล: พบปัญหา

ถ้าเลือก dependency ตามบทที่ 3:

- Spring Web MVC
- Spring Data JPA
- Validation
- PostgreSQL Driver
- Lombok

แล้วรัน `.\gradlew.bat bootRun` ทันที แอป start ไม่ขึ้น เพราะมี JPA/PostgreSQL แต่ยังไม่ได้ตั้งค่า datasource

error หลัก:

```text
Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
Failed to determine a suitable driver class
```

ข้อเสนอแก้หนังสือ:

- ทางเลือกที่แนะนำ: บทที่ 3 ให้เลือกเฉพาะ `Spring Web MVC`, `Validation`, `Lombok`
- ย้าย `Spring Data JPA` และ `PostgreSQL Driver` ไปเพิ่มในบทที่ 11
- หรือถ้าจะเลือก JPA/PostgreSQL ตั้งแต่บทที่ 3 ต้องใส่ `application.properties` สำหรับ datasource ตั้งแต่บทที่ 3 ด้วย

ระดับความรุนแรง: สูง เพราะมือใหม่จะติดตั้งแต่โปรเจกต์แรก

### บท 5: Controller แรก

ผล: ผ่าน หลังแก้ dependency ช่วงต้นให้ไม่มี JPA/PostgreSQL ก่อน

ตรวจ endpoint:

```text
GET /hello   -> Hello Spring Boot
GET /health  -> OK
GET /version -> SecureAdmin API v1
```

ข้อสังเกต:

- เนื้อหาบท 5 ใช้ได้จริง
- ถ้าใส่ Spring Security ตั้งแต่ต้น endpoint จะโดนล็อก แต่หนังสือไม่ได้ให้ใส่ Security ตอนต้น จึงถือว่าไม่ใช่ปัญหาของบท 5

### บท 7-8: User API in-memory และแยก Service

ผล: ผ่าน

ตรวจ flow:

```text
POST /api/v1/users
GET  /api/v1/users
GET  /api/v1/users/1
DELETE /api/v1/users/1
```

ผลลัพธ์ถูกต้องตามหนังสือ

ข้อสังเกต:

- เนื้อหาในบท 7-8 กับไฟล์ `examples/chapter-07-*` และ `examples/chapter-08-*` ไม่เหมือนกัน 100%
- ในหนังสือใช้ `RuntimeException` และ delete คืน string
- ใน examples ใช้ `ResponseEntity` และตอบ 404/204 ดีขึ้น

ข้อเสนอ:

- เลือกแนวทางเดียวให้ตรงกัน หรือระบุในหนังสือว่า examples เป็นเวอร์ชันปรับปรุง

ระดับความรุนแรง: กลาง

### บท 11: PostgreSQL config

ผล: ผ่าน เมื่อมี PostgreSQL local และ database `secure_admin`

ตรวจแล้ว:

- สร้าง database `secure_admin`
- ตั้งค่า datasource ใน `application.properties`
- `bootRun` start ได้

ข้อสังเกต:

- บท 11 snippet ยังใช้ `spring-boot-starter-web` และ `spring-boot-starter-test`
- แต่โปรเจกต์ Spring Boot 4 จาก Initializr ใช้ `spring-boot-starter-webmvc` และ test starter แบบแยก เช่น `spring-boot-starter-webmvc-test`

ข้อเสนอ:

- ปรับ snippet dependency ให้เป็น Spring Boot 4 ทั้งเล่ม

ระดับความรุนแรง: กลางถึงสูง เพราะทำให้ผู้อ่านสับสนระหว่าง Boot 3 กับ Boot 4

### บท 12-13: Entity และ Repository

ผล: ผ่าน

ตรวจแล้ว:

- `User` entity compile ผ่าน
- `Role` และ `UserStatus` compile ผ่าน
- `UserRepository extends JpaRepository<User, Long>` compile ผ่าน
- context test ผ่าน

ข้อสังเกต:

- หลังบท 13 endpoint เดิมยังไม่ได้ใช้ database จริง เพราะ `UserService` ยังเป็น in-memory จนถึงบท 14
- หนังสืออธิบายได้พอใช้ แต่ควรย้ำว่า repository ถูกสร้างแล้ว แต่ endpoint จะใช้ DB จริงในบทถัดไป

ระดับความรุนแรง: ต่ำ

### บท 14: User CRUD กับฐานข้อมูล

ผล: ผ่าน

ตรวจ flow:

```text
POST /api/v1/users
GET  /api/v1/users
```

response หลัง create:

```json
{"id":1,"username":"john","email":"john@example.com","role":"USER","status":"ACTIVE"}
```

ตรวจแล้วว่า response ไม่มี `password`

ระดับความรุนแรง: ผ่าน

### Final project

ผล: ผ่าน

ตอน validation ใช้ Gradle wrapper จาก validation project รัน:

```powershell
.\gradlew.bat -p "D:\code\IDE\Visual Studio Code\spring-boot-tutorial-book\examples\final-secure-admin-api" test bootJar
```

ผล:

```text
BUILD SUCCESSFUL
```

ข้อสังเกต:

- final project build ได้จริง
- แปลว่าโค้ดปลายทางแข็งแรงกว่าบาง snippet ระหว่างบท

## ประเด็นที่ต้องแก้ก่อนเผยแพร่

### 1. Dependency ในบทที่ 3 ทำให้แอป start ไม่ขึ้น

สถานะ: แก้แล้ว เพราะเป็น blocker สำหรับมือใหม่

แนวทางที่ปรับแล้ว:

บทที่ 3:

```text
Spring Web MVC
Validation
Lombok
```

บทที่ 11 ค่อยเพิ่ม:

```text
Spring Data JPA
PostgreSQL Driver
```

### 2. User status ไม่สอดคล้องกัน

สถานะ: แก้แล้ว โดยเลือกใช้ `INACTIVE` เป็นคำเดียวทั้งเล่ม

จุดที่ปรับให้ตรงกัน:

- `UserStatus`
- บท 12, 25, 29, 30, 31, 32, 33
- Postman collection
- final project
- requests.http

### 3. Dependency snippet ยังปน Spring Boot 3/4

สถานะ: แก้แล้วในบทหลักและ snippet ที่พบจาก validation

ตัวอย่างที่ตรวจและปรับแล้ว:

- `spring-boot-starter-oauth2-resource-server`
- `spring-boot-starter-security-oauth2-resource-server`
- `spring-boot-starter-webmvc-test`

หนังสือเลือก Spring Boot 4 เป็นหลัก ดังนั้น snippet ควรใช้ชื่อ dependency ของ Spring Boot 4 ให้สม่ำเสมอ

### 4. Final project ไม่มี Gradle wrapper ของตัวเอง

สถานะ: แก้แล้ว โดยเพิ่ม Gradle wrapper ให้ `examples/final-secure-admin-api/`

ตอนนี้ผู้อ่านสามารถเข้าโฟลเดอร์ final project แล้วรัน:

```powershell
.\gradlew.bat test
.\gradlew.bat bootJar
```

### 5. ควรเพิ่ม validation checklist รายบท

ตอนนี้หลายบทมี checkpoint แล้ว แต่ยังไม่เท่ากันทุกบท

ควรเพิ่มรูปแบบเดียวกัน:

```text
Command ที่ต้องผ่าน
Endpoint ที่ต้องลอง
Expected response
ถ้าพังให้เช็กอะไร
```

## ข้อสรุป

หนังสือสอนได้ถูกทิศทางและ final project build ได้จริง แต่ยังมี blocker สำคัญช่วงบท 3 จากการเลือก JPA/PostgreSQL เร็วเกินไป

ถ้าแก้ dependency flow, ทำ enum status ให้ตรงกัน และปรับ snippet ให้เป็น Spring Boot 4 ทั้งเล่ม หนังสือจะพร้อมขึ้นอีกระดับมาก
