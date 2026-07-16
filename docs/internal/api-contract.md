---
title: API Contract
description: API surface หลักที่หนังสือ Spring Boot และ frontend ต่อไปควรยึดเป็น contract
---

# API Contract

เอกสารนี้สรุป endpoint และ response shape หลักของ final backend API เพื่อใช้ตรวจความสอดคล้องของหนังสือ, examples, final project และ frontend book ที่จะต่อยอด

## Base Path

```text
/api/v1
```

## Auth

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
GET  /api/v1/auth/me
```

ตัวอย่าง `LoginRequest`:

```json
{
  "email": "admin@example.com",
  "password": "Admin1234!"
}
```

ตัวอย่าง `LoginResponse`:

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "email": "admin@example.com",
    "role": "ADMIN"
  }
}
```

## Users

```text
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
PUT    /api/v1/users/{id}
DELETE /api/v1/users/{id}
```

ช่วงต้นเล่ม endpoint เหล่านี้ใช้สอน CRUD และ architecture ก่อนจะถูกต่อยอดด้วย auth/admin behavior

## Admin Users

```text
GET   /api/v1/admin/users
PATCH /api/v1/admin/users/{id}/role
PATCH /api/v1/admin/users/{id}/status
```

query หลัก:

```text
page
size
search
role
status
sort
```

ตัวอย่าง paged response:

```json
{
  "items": [
    {
      "id": 1,
      "email": "admin@example.com",
      "role": "ADMIN",
      "status": "ACTIVE"
    }
  ],
  "page": 0,
  "size": 10,
  "totalItems": 1,
  "totalPages": 1
}
```

## Audit Logs

```text
GET /api/v1/admin/audit-logs
```

audit log ต้องไม่บันทึก password, JWT หรือ secret ลง detail

## Error Contract

API ควรตอบ error ที่ frontend และผู้เรียนอ่านได้:

```json
{
  "code": "VALIDATION_FAILED",
  "message": "Validation failed",
  "errors": {
    "email": ["must be a well-formed email address"]
  }
}
```

status code ที่ต้องรักษา:

```text
400 validation error
401 no/invalid token
403 authenticated but forbidden
404 resource not found
409 conflict เช่น email ซ้ำ
500 unexpected server error
```
