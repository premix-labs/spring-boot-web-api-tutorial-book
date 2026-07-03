---
title: รายงานตรวจทำตามหนังสือ
description: บันทึกผลการทดลองสร้างโปรเจกต์ใหม่และทำตามหนังสือทีละบท
---

# รายงานตรวจทำตามหนังสือ

หน้านี้เป็น validation report ฉบับใช้งานปัจจุบันตาม `Book Documentation Standard v1`

## Validation Strategy

ใช้สองแหล่งตรวจ:

```text
examples/chapter-*
examples/final-backend-api
```

- `examples/chapter-*` ใช้ตรวจว่าบทนั้นมี code shape ที่สอดคล้อง
- `examples/final-backend-api` ใช้เป็น reference ของ end-state

## สถานะการตรวจ

- 2026-07-03: เพิ่ม `Book Documentation Standard v1` ใน `docs/internal/README.md` และรัน `npm run build` ผ่าน
- บท 1-10 Foundation และ REST API: ผ่านรอบ draft
- บท 11-15 PostgreSQL, JPA, Repository, CRUD: ผ่านรอบ draft
- บท 16-20 Validation, Exception, DTO, Pagination: ผ่านรอบ draft
- บท 21-26 Register/Login/JWT/Current User/Protected endpoints: ผ่านรอบ draft
- บท 27-31 Admin, Role, Change Status, Audit Log: ผ่านรอบ draft
- บท 32-40 Production Ready, Flyway, Test, Docker, Deploy: ผ่านรอบ draft
- Final project build/test/bootJar: เคยผ่านแล้วตามรายงาน legacy
- Docker Compose smoke test: เคยผ่านแล้วตามรายงาน legacy

## Smoke Tests ที่ต้องรักษา

```text
GET /hello                            200 ในบทต้น
POST /api/v1/users                    201
GET /api/v1/users                     200
POST /api/v1/auth/register            200
POST /api/v1/auth/login               200
GET /api/v1/auth/me                   200 เมื่อส่ง token
GET /api/v1/admin/users               200 เมื่อเป็น admin
GET /api/v1/admin/users               403 เมื่อเป็น user
PATCH /api/v1/admin/users/{id}/role   200 หรือ validation error
PATCH /api/v1/admin/users/{id}/status 200 หรือ self-protection error
```

## Commands

Website:

```powershell
npm run build
```

Final project:

```powershell
cd examples/final-backend-api
.\gradlew.bat clean test bootJar
docker compose config
```

## ประเด็นที่ต้องบันทึกเมื่อเจอ

- command ในบทใช้ไม่ได้จริง
- chapter ใช้ class, annotation หรือ package ที่ยังไม่เคยสร้าง
- expected response ไม่ตรงกับ runtime
- Gradle dependency ไม่ตรงกับ Spring Boot version
- migration หรือ entity ไม่ตรงกัน
- test fail หลังแก้ chapter
- Docker config ไม่ตรงกับ docs
- secret หรือ connection string จริงหลุดเข้า tracked files
