# Chapter 17 - Exception Handling

ตัวอย่างนี้ใช้ประกอบบทที่ 17 สำหรับจัดการ error กลางด้วย `@RestControllerAdvice`

ไฟล์ที่ใช้:

- `UserNotFoundException.java`
- `DuplicateUserException.java`
- `ErrorResponse.java`
- `GlobalExceptionHandler.java`

## HTTP status ที่ใช้

| กรณี               | Status            |
| ------------------ | ----------------- |
| หา user ไม่เจอ     | `404 Not Found`   |
| username/email ซ้ำ | `409 Conflict`    |
| validation ไม่ผ่าน | `400 Bad Request` |
