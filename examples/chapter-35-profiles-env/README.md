# Chapter 35 - Profiles และ Environment Variables

ตัวอย่างนี้ใช้ประกอบบทที่ 35 สำหรับแยก config ตาม environment

ไฟล์ที่ใช้:

- `application.properties`
- `application-dev.properties`
- `application-prod.properties`

production ต้องส่งค่าผ่าน environment variables ไม่ควรใช้ default secret ในไฟล์ config
