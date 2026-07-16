# Chapter 27 - Role USER และ ADMIN

ตัวอย่างนี้ใช้ประกอบบทที่ 27 สำหรับทบทวน role และ status ใน `User` entity

ไฟล์ที่ใช้:

- `Role.java`
- `UserStatus.java`
- `User.java`
- `JwtService.java`

ถ้าทำตามบทก่อนหน้ามาแล้ว ไฟล์เหล่านี้อาจมีอยู่แล้ว ให้ใช้ตัวอย่างนี้ตรวจว่าค่า role/status และ JWT claim ตรงกัน

## SQL สำหรับสร้าง admin คนแรก

```sql
UPDATE users
SET role = 'ADMIN'
WHERE email = 'admin@example.com';
```
