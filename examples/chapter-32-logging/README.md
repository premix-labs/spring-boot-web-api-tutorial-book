# Chapter 32 - Logging

ตัวอย่างนี้ใช้ประกอบบทที่ 32 สำหรับเพิ่ม logging ให้ service

ไฟล์ที่ใช้:

- `AuthService.java`: ตัวอย่าง log ตอน login
- `application-dev.properties`: เปิด debug log สำหรับ package ของเรา
- `application-prod.properties`: ลด log level ใน production

## ข้อควรระวัง

ห้าม log password, JWT token เต็มก้อน, secret หรือ database password

