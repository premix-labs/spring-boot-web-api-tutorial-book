# Chapter 08 - แยก Layer

ตัวอย่างนี้ใช้ประกอบบทที่ 8 เพื่อย้าย business logic ออกจาก controller ไปไว้ใน service

ไฟล์ที่ใช้:

- `UserService.java`: จัดการ list, id, การค้นหา และการลบ user
- `UserController.java`: รับ request แล้วเรียก service

ให้นำไฟล์ไปวางในโปรเจกต์ Spring Boot ตาม path นี้:

```text
src/main/java/com/example/secureadmin/service/UserService.java
src/main/java/com/example/secureadmin/controller/UserController.java
```

ใช้ `User.java` จากตัวอย่างบทที่ 7 ต่อได้เลย

## Flow หลังแยก layer

```text
Client/Postman
  -> UserController
  -> UserService
  -> List<User>
```

## จุดที่ควรสังเกต

- `UserController` ไม่สร้าง id เองแล้ว
- `UserController` ไม่วนลูปค้นหา user เองแล้ว
- `UserService` เป็นที่รวม logic หลักของ user
- เมื่อย้ายไปใช้ database ในบทถัดไป จะเปลี่ยนจาก `List<User>` เป็น `UserRepository` ได้ง่ายขึ้น

