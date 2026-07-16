---
title: Release Checklist
description: รายการตรวจคุณภาพก่อนเผยแพร่หนังสือและตัวอย่างโค้ด
---

# Release Checklist

ใช้ checklist นี้ก่อน publish หนังสือหรือ release ใหญ่

## Manuscript

- ทุกบทมี goal, prerequisites, steps, expected result และ checkpoint
- route, port, package name และ command ตรงกับ project จริง
- ไม่มีบทที่อ้างถึงไฟล์หรือ class ที่ยังไม่เคยสร้าง
- ไม่มี code block ที่ copy แล้ว compile ไม่ได้โดยไม่อธิบาย
- production warning ครบสำหรับ secrets, JWT, CORS, Docker และ logging
- chapter examples ตรงกับบทที่เกี่ยวข้อง

## Website

```powershell
npm run build
```

ตรวจ:

- sidebar ครบ
- search ใช้งานได้
- internal links ไม่เสีย
- ภาษาไทยไม่เพี้ยน

## Final Project

```powershell
cd examples/final-backend-api
.\gradlew.bat clean test bootJar
docker compose config
```

ต้องตรวจอย่างน้อย:

- register/login
- current user
- admin users
- role/status changes
- audit logs
- OpenAPI/Swagger
- Flyway migration
- Docker Compose config

## Security and Secrets

- ไม่มี `.env` จริงถูก track
- `.env.example` ไม่มี secret จริง
- tracked `application*.properties` ไม่เก็บ production secrets
- JWT signing key, database password และ SMTP credentials มาจาก environment ใน production
- error response ไม่ leak stack trace
- CORS policy แยก dev/prod

## Sign-off

ก่อน release ให้ update:

```text
docs/internal/manuscript-status.md
docs/internal/validation-report.md
```
