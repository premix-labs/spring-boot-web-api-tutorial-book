---
title: 10 - สรุป REST API พื้นฐาน
description: ทบทวนสิ่งที่สร้างก่อนเข้าสู่ฐานข้อมูล
---

## เป้าหมายของบท

บทนี้สรุปสิ่งที่ทำในภาค REST API ก่อนเข้าสู่ภาคฐานข้อมูล เพื่อให้มั่นใจว่าผู้อ่านเข้าใจ controller, service, HTTP method และการทดสอบ API

## สิ่งที่สร้างแล้ว

ตอนนี้ project ควรมี endpoint:

```text
GET    /hello
GET    /health
GET    /version
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
DELETE /api/v1/users/{id}
```

และควรมี class หลัก:

```text
HelloController
UserController
UserService
User
```

## Flow ที่ควรเข้าใจ

ตอนเรียก:

```text
POST /api/v1/users
```

flow คือ:

```text
Postman
  -> UserController.createUser()
  -> UserService.createUser()
  -> List<User>
  -> JSON response
```

## สิ่งที่ยังเป็นข้อจำกัด

ระบบตอนนี้ยังเป็น training version:

- ข้อมูลหายเมื่อ restart
- ยังไม่มี validation
- ยังไม่มี DTO
- ยังไม่มี exception handler
- ยังไม่มี database
- ยังไม่มี security

ข้อจำกัดเหล่านี้ไม่ได้แปลว่าโค้ดผิด แต่เป็นจุดที่จะค่อย ๆ พัฒนาในภาคถัดไป

## Mini Review

ตอบคำถามเหล่านี้ก่อนเข้าสู่ภาค database:

1. `@RestController` ทำอะไร
2. `@RequestMapping("/api/v1/users")` ช่วยอะไร
3. `@PathVariable Long id` รับค่าจากส่วนไหนของ URL
4. `@RequestBody User user` รับข้อมูลจากส่วนไหนของ request
5. ทำไมต้องแยก `UserService`
6. ทำไม in-memory list ไม่เหมาะกับงานจริง
7. ถ้า restart application ข้อมูล user จะเกิดอะไรขึ้น

## เตรียมเข้าสู่ภาคฐานข้อมูล

ในภาคถัดไป เราจะเปลี่ยนจาก:

```text
List<User>
```

ไปเป็น:

```text
PostgreSQL + Spring Data JPA
```

และเพิ่ม:

```text
UserRepository extends JpaRepository<User, Long>
```

Controller จะยังคล้ายเดิม แต่ service จะเรียก repository แทน list

## Checkpoint ก่อนจบภาค

ตรวจให้ครบ:

- รัน app ได้
- สร้าง user ผ่าน Postman ได้
- อ่าน user ทั้งหมดได้
- อ่าน user ตาม id ได้
- ลบ user ได้
- เข้าใจว่า controller กับ service ต่างกันอย่างไร

## แบบฝึกหัดท้ายบท

วาด flow ของ `GET /api/v1/users/{id}` โดยแยกเป็นกล่อง:

```text
Postman -> UserController -> UserService -> List<User>
```

จากนั้นเขียนอธิบายเอง 3-5 บรรทัดว่าแต่ละกล่องทำหน้าที่อะไร
