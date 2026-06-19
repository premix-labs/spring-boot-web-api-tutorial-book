# Chapter 19 - Response Format

ตัวอย่างนี้ใช้ประกอบบทที่ 19 สำหรับห่อ success response ด้วย `ApiResponse<T>`

ไฟล์ที่ใช้:

- `ApiResponse.java`
- `UserController.java`

ใช้ร่วมกับ DTO และ service จากบทที่ 18

## แนวทางในตัวอย่าง

- success response ใช้ `ApiResponse<T>`
- error response ยังใช้ `ErrorResponse`
- delete endpoint ยังตอบ `204 No Content`

