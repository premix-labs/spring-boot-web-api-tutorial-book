# Chapter 14 - User CRUD กับฐานข้อมูล

ตัวอย่างนี้ใช้ประกอบบทที่ 14 สำหรับทำ CRUD API โดยใช้ PostgreSQL จริง

ไฟล์ที่ใช้:

- `UserService.java`
- `UserController.java`
- `requests.http`

ต้องใช้ร่วมกับไฟล์จากบทก่อนหน้า:

- `Role.java`
- `UserStatus.java`
- `User.java`
- `UserRepository.java`

## วิธีทดสอบ

1. รัน PostgreSQL
2. รัน Spring Boot
3. เปิด `requests.http`
4. ยิง request ตามลำดับจากบนลงล่าง

บทนี้ยังไม่มี `PUT` endpoint ให้ไปดูเวอร์ชันที่เติม update แล้วใน `chapter-15-update-user`

