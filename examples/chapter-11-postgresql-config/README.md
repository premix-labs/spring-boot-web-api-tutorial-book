# Chapter 11 - ตั้งค่า PostgreSQL

ตัวอย่างนี้ใช้ประกอบบทที่ 11 สำหรับตั้งค่า Spring Boot ให้เชื่อม PostgreSQL

ไฟล์ที่ใช้:

- `build.gradle-snippet.gradle`: dependency ที่ต้องมี
- `application.properties`: datasource และ JPA config
- `create-database.sql`: SQL สำหรับสร้าง database

## วิธีใช้

1. เพิ่ม dependency จาก `build.gradle-snippet.gradle` เข้าไปใน `build.gradle`
2. สร้าง database ด้วย `create-database.sql`
3. คัดลอก config จาก `application.properties` ไปไว้ที่ `src/main/resources/application.properties`
4. ปรับ username/password ให้ตรงกับเครื่องของคุณ
5. รัน Spring Boot

## คำเตือน

ค่า password ในไฟล์ตัวอย่างใช้เพื่อการเรียนเท่านั้น งานจริงควรใช้ environment variables หรือ secret manager
