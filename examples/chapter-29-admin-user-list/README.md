# Chapter 29 - Admin ดูรายชื่อผู้ใช้

ตัวอย่างนี้ใช้ประกอบบทที่ 29 สำหรับสร้าง admin user list พร้อม pagination และ filter

ไฟล์ที่ใช้:

- `UserRepository.java`
- `AdminUserService.java`
- `AdminUserController.java`
- `requests.http`

Endpoint นี้อยู่ใต้ `/api/v1/admin/**` จึงต้องใช้ admin token
