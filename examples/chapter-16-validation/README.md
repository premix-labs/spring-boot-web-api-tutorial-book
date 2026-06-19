# Chapter 16 - Validation

ตัวอย่างนี้ใช้ประกอบบทที่ 16 สำหรับเพิ่ม Bean Validation ให้ request DTO

ไฟล์ที่ใช้:

- `build.gradle-snippet.gradle`: dependency validation
- `CreateUserRequest.java`: request สำหรับสร้าง user
- `UpdateUserRequest.java`: request สำหรับแก้ username/email
- `UserService.java`: service ที่รับ DTO แทน entity
- `UserController.java`: ตัวอย่าง controller ที่ใส่ `@Valid`
- `requests.http`: request สำหรับทดสอบ validation

ต้องใช้ร่วมกับ entity และ repository จากบทก่อนหน้า

## จุดสำคัญ

Controller ต้องใส่ `@Valid` หน้า `@RequestBody` ไม่อย่างนั้น validation annotation ใน DTO จะไม่ทำงาน
