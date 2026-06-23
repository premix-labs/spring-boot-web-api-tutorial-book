---
title: Postman Collection
description: วิธี import และใช้ Postman collection สำหรับทดสอบ final project
---
หนังสือเล่มนี้มี Postman collection สำหรับโปรเจกต์ `final-backend-api` แล้ว

ไฟล์อยู่ที่:

```text
examples/final-backend-api/postman/backend-api.postman_collection.json
examples/final-backend-api/postman/backend-api-local.postman_environment.json
```

## วิธี import เข้า Postman

1. เปิด Postman
2. กด `Import`
3. เลือกไฟล์ `backend-api.postman_collection.json`
4. import อีกครั้งด้วยไฟล์ `backend-api-local.postman_environment.json`
5. เลือก environment ชื่อ `Backend API Local`

## ตัวแปรใน environment

```text
baseUrl       = http://localhost:8080
adminEmail    = admin@example.com
adminPassword = admin12345
token         = เว้นว่างไว้ก่อน
```

เมื่อกด request `Login Bootstrap Admin` สำเร็จ collection จะบันทึก JWT ลงตัวแปร `token` ให้อัตโนมัติ

## ลำดับการทดสอบที่แนะนำ

ให้รันตามลำดับนี้:

1. `Health Check`
2. `Register Normal User`
3. `Login Bootstrap Admin`
4. `Get Current User`
5. `List Users`
6. `Get Registered User By Id`
7. `Change Registered User Role To ADMIN`
8. `Change Registered User Status To INACTIVE`
9. `Change Registered User Status Back To ACTIVE`
10. `List Audit Logs`

## Negative cases

collection มีตัวอย่าง request ที่ควร fail ด้วย:

- `Register With Invalid Email`: ควรได้ `400 VALIDATION_ERROR`
- `Admin Users Without Token`: ควรได้ `401`

กลุ่มนี้ช่วยให้ผู้อ่านเข้าใจว่า API ที่ดีต้องทดสอบทั้งกรณีสำเร็จและกรณีผิดพลาด

## ก่อนรัน collection

ต้องรัน API ให้พร้อมก่อน:

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
cd examples/final-backend-api
.\gradlew.bat bootRun
```

ถ้ายังไม่ได้ตั้ง `JAVA_HOME` บนเครื่องนี้ ให้ตั้งก่อน:

```powershell
$env:JAVA_HOME="C:\path\to\jdk-25"
```

## หมายเหตุ

request `Register Normal User` จะสร้าง username/email ใหม่จาก timestamp ทุกครั้ง เพื่อเลี่ยงปัญหา email ซ้ำตอนกดรันหลายรอบ
