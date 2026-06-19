# Chapter 18 - DTO

ตัวอย่างนี้ใช้ประกอบบทที่ 18 สำหรับแยก Entity ออกจาก API request/response

ไฟล์ที่ใช้:

- `CreateUserRequest.java`
- `UpdateUserRequest.java`
- `UserResponse.java`
- `UserService.java`
- `UserController.java`

ต้องใช้ร่วมกับ `User`, `Role`, `UserStatus`, `UserRepository` และ exception จากบทก่อนหน้า

## จุดสำคัญ

`UserResponse` ไม่มี password ดังนั้น API จะไม่ส่ง password กลับไปหา client

