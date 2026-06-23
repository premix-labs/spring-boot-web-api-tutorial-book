# Chapter 07 - User API แบบไม่ใช้ฐานข้อมูล

ตัวอย่างนี้ใช้ประกอบบทที่ 7 เพื่อฝึกสร้าง REST API โดยยังไม่ต่อ database จริง

ไฟล์ที่ใช้:

- `User.java`: model สำหรับรับและส่งข้อมูลผู้ใช้
- `UserController.java`: controller ที่เก็บข้อมูลด้วย `List<User>` ใน memory

ให้นำไฟล์ไปวางในโปรเจกต์ Spring Boot ตาม path นี้:

```text
src/main/java/com/example/backendapi/model/User.java
src/main/java/com/example/backendapi/controller/UserController.java
```

## ทดสอบ API

รัน Spring Boot แล้วทดสอบด้วยคำสั่ง:

```powershell
curl.exe http://localhost:8080/api/v1/users
```

สร้าง user:

```powershell
curl.exe -X POST http://localhost:8080/api/v1/users `
  -H "Content-Type: application/json" `
  -d "{\"username\":\"john\",\"email\":\"john@example.com\"}"
```

ดึง user ตาม id:

```powershell
curl.exe http://localhost:8080/api/v1/users/1
```

ลบ user:

```powershell
curl.exe -X DELETE http://localhost:8080/api/v1/users/1
```

## ข้อควรจำ

ข้อมูลในตัวอย่างนี้หายทุกครั้งที่ restart application เพราะเก็บไว้ใน memory เท่านั้น

