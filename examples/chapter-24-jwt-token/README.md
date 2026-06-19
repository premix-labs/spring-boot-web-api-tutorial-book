# Chapter 24 - JWT Token

ตัวอย่างนี้ใช้ประกอบบทที่ 24 สำหรับสร้าง JWT จริงหลัง login

ไฟล์ที่ใช้:

- `build.gradle-snippet.gradle`
- `application.properties`
- `JwtProperties.java`
- `SecurityConfig.java`
- `JwtService.java`
- `AuthService.java`

ตัวอย่างนี้ใช้ Spring Boot 4 starter ชื่อ `spring-boot-starter-security-oauth2-resource-server`

ถ้าใช้ Spring Boot 3 ให้เปลี่ยนเป็น `spring-boot-starter-oauth2-resource-server`

## ข้อควรระวัง

ไฟล์ `SecurityConfig.java` มี `PasswordEncoder` bean แล้ว ถ้าโปรเจกต์ยังมี `SecurityBeansConfig.java` จากบท 22 ให้เหลือ bean นี้ไว้แค่ที่เดียว

