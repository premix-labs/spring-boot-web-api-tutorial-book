# Chapter 15 - แก้ไขข้อมูลผู้ใช้

ตัวอย่างนี้ใช้ประกอบบทที่ 15 เป็นเวอร์ชันที่เพิ่ม `PUT /api/v1/users/{id}` แล้ว

ไฟล์ที่ใช้:

- `UserService.java`
- `UserController.java`
- `requests.http`

ต้องใช้ร่วมกับไฟล์จากบทก่อนหน้า:

- `Role.java`
- `UserStatus.java`
- `User.java`
- `UserRepository.java`

## Field ที่ endpoint นี้แก้ได้

- `username`
- `email`

ยังไม่ให้แก้ `password`, `role` และ `status` เพราะ field เหล่านี้ควรมี flow เฉพาะในบทหลัง
