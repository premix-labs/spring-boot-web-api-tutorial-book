# Chapter 21 - ออกแบบ Register

ตัวอย่างนี้ใช้ประกอบบทที่ 21 สำหรับเพิ่ม endpoint สมัครสมาชิก

ไฟล์ที่ใช้:

- `RegisterRequest.java`
- `AuthController.java`
- `AuthService.java`
- `requests.http`

ต้องใช้ร่วมกับ `User`, `UserRepository`, `UserResponse`, `ApiResponse` และ exception จากบทก่อนหน้า

## Endpoint

```text
POST /api/v1/auth/register
```

บทนี้ยังไม่ได้ hash password เพื่อให้เห็น register flow ก่อน บทที่ 22 จะปรับให้ปลอดภัยขึ้นทันที
