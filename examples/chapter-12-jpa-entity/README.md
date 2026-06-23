# Chapter 12 - JPA และ Entity

ตัวอย่างนี้ใช้ประกอบบทที่ 12 สำหรับสร้าง entity และ enum ที่ map กับ PostgreSQL

ให้นำไฟล์ไปวางตาม path นี้:

```text
src/main/java/com/example/backendapi/model/Role.java
src/main/java/com/example/backendapi/model/UserStatus.java
src/main/java/com/example/backendapi/model/User.java
```

หลังจากตั้งค่า `spring.jpa.hibernate.ddl-auto=update` แล้วรัน application Hibernate จะสร้าง table `users` จาก `User` entity

