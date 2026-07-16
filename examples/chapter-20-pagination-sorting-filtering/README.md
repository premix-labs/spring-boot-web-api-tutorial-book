# Chapter 20 - Pagination, Sorting, Filtering

ตัวอย่างนี้ใช้ประกอบบทที่ 20 สำหรับปรับ endpoint รายชื่อ user ให้รองรับข้อมูลจำนวนมาก

ไฟล์ที่ใช้:

- `PageResponse.java`
- `UserRepository.java`
- `UserService.java`
- `UserController.java`
- `requests.http`

ต้องใช้ร่วมกับ DTO, exception handler และ `ApiResponse` จากบทก่อนหน้า

## Endpoint เป้าหมาย

```text
GET /api/v1/users?page=0&size=20&sortBy=createdAt&direction=desc&keyword=john
```
