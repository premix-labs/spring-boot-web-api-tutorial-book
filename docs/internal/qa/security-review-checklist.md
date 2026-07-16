---
title: Security Review Checklist
description: รายการตรวจ security สำหรับหนังสือและตัวอย่าง Spring Boot Web API
---

# Security Review Checklist

ใช้ checklist นี้เมื่อแก้ auth, admin, production-ready, Docker หรือ deployment docs

## Secrets

- ไม่มี database password จริงใน tracked config ของ end-state/final project
- ไม่มี JWT signing key จริงใน repo
- `.env` ถูก ignore และมี `.env.example`
- docs ใช้ placeholder ที่ชัดเจน
- README ไม่สอนให้ commit secret จริง

## Authentication

- password เก็บด้วย hash เท่านั้น
- login failure ไม่ leak รายละเอียดเกินจำเป็น
- JWT signing key ไม่ hard-code สำหรับ production
- current user endpoint ตรวจ token จริง

## Authorization

- admin endpoint ใช้ backend authorization ไม่ใช่แค่ frontend guard
- no token ได้ `401`
- authenticated แต่ role ไม่พอได้ `403`
- role update/status update มี validation
- self-protection rule มี test หรือ smoke test

## Error Handling

- validation error อ่านได้
- business error มี code/message ชัด
- production ไม่ส่ง stack trace
- unexpected error ถูก log server-side

## Data Protection

- API ไม่ส่ง password hash หรือ secret field
- audit log ไม่บันทึก JWT หรือ password
- sensitive config อ่านจาก environment ใน production

## Infrastructure

- Docker Compose ไม่ฝัง production secret จริง
- CORS ไม่เปิด wildcard สำหรับ production โดยไม่อธิบาย
- Flyway migration run ก่อน Hibernate validation ใน final project
