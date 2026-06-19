---
title: 06 - HTTP และ REST API
description: เข้าใจ GET, POST, PUT, DELETE, JSON และ status code
---

## เป้าหมายของบท

บทนี้ปูพื้นฐาน HTTP และ REST API ก่อนสร้าง User API เพราะถ้าเข้าใจ method, path, body และ status code ดี การ debug Spring Boot จะง่ายขึ้นมาก

หลังจบบทนี้ ผู้อ่านควรเข้าใจ:

- HTTP method ใช้ทำอะไร
- REST API ควรตั้ง path อย่างไร
- JSON request/response คืออะไร
- status code บอกผลลัพธ์ของ request อย่างไร

## HTTP request มีอะไรบ้าง

Request ที่ client ส่งเข้า backend มักมีส่วนสำคัญ:

```text
Method  : GET, POST, PUT, DELETE
URL     : /api/v1/users
Headers : Content-Type, Authorization
Body    : JSON payload
```

ตัวอย่าง request:

```http
POST /api/v1/users
Content-Type: application/json
```

```json
{
  "username": "john",
  "email": "john@example.com"
}
```

## HTTP method ที่ใช้บ่อย

| Method | ใช้ทำอะไร | ตัวอย่าง |
| --- | --- | --- |
| `GET` | อ่านข้อมูล | `GET /api/v1/users` |
| `POST` | สร้างข้อมูล | `POST /api/v1/users` |
| `PUT` | แก้ไขข้อมูลทั้งชุด | `PUT /api/v1/users/1` |
| `PATCH` | แก้ไขบาง field | `PATCH /api/v1/admin/users/1/status` |
| `DELETE` | ลบข้อมูล | `DELETE /api/v1/users/1` |

## REST API ควรตั้ง path อย่างไร

REST API ควรมองข้อมูลเป็น resource เช่น `users`, `auth`, `admin/users`

ตัวอย่างที่ดี:

```text
GET    /api/v1/users
GET    /api/v1/users/1
POST   /api/v1/users
PUT    /api/v1/users/1
DELETE /api/v1/users/1
```

ตัวอย่างที่ควรหลีกเลี่ยง:

```text
GET /getUsers
GET /deleteUser/1
POST /createNewUser
```

เหตุผลคือ HTTP method บอก action อยู่แล้ว path จึงควรเน้นชื่อ resource

## JSON คืออะไร

JSON คือรูปแบบข้อมูลที่ REST API ใช้รับส่งกันบ่อยมาก

ตัวอย่าง request body:

```json
{
  "username": "john",
  "email": "john@example.com"
}
```

ตัวอย่าง response body:

```json
{
  "id": 1,
  "username": "john",
  "email": "john@example.com"
}
```

Spring Boot สามารถแปลง JSON เป็น Java object ได้ด้วย `@RequestBody` และแปลง Java object กลับเป็น JSON ได้อัตโนมัติเมื่อใช้ `@RestController`

## Status code ที่ควรรู้

| Status | ความหมาย | ใช้เมื่อไร |
| --- | --- | --- |
| `200 OK` | สำเร็จ | อ่านหรือแก้ไขข้อมูลสำเร็จ |
| `201 Created` | สร้างสำเร็จ | สร้าง resource ใหม่ |
| `400 Bad Request` | request ไม่ถูกต้อง | validation fail |
| `401 Unauthorized` | ยังไม่ login | ไม่มี token หรือ token ผิด |
| `403 Forbidden` | ไม่มีสิทธิ์ | login แล้วแต่ role ไม่พอ |
| `404 Not Found` | ไม่พบข้อมูล | หา user id นั้นไม่เจอ |
| `409 Conflict` | ข้อมูลชนกัน | email ซ้ำ |
| `500 Internal Server Error` | server error | bug หรือ error ที่ยังไม่ได้จัดการ |

## API ที่จะสร้างในภาคนี้

ในภาค REST API พื้นฐาน เราจะสร้าง User API แบบยังไม่ต่อ database:

```text
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
DELETE /api/v1/users/{id}
```

ข้อมูลจะเก็บใน memory ด้วย `List<User>` ก่อน เพื่อให้เข้าใจ HTTP และ controller flow โดยไม่ต้องแก้ปัญหา database ตั้งแต่ต้น

## Checkpoint

ก่อนอ่านบทถัดไป ให้ตอบได้:

1. `GET` กับ `POST` ต่างกันอย่างไร
2. `@RequestBody` ใช้กับข้อมูลส่วนไหนของ HTTP request
3. ถ้าหา user ไม่เจอ ควรตอบ status code อะไร
4. ถ้า email ซ้ำ ควรตอบ status code อะไร

## แบบฝึกหัดท้ายบท

ออกแบบ endpoint สำหรับ register/login โดยยังไม่ต้องเขียนโค้ด แล้วเทียบกับแนวทางนี้:

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
GET  /api/v1/auth/me
```
