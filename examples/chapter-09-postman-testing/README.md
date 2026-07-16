# Chapter 09 - ทดสอบ API ด้วย Postman

ตัวอย่างนี้ใช้ประกอบบทที่ 9 สำหรับทดสอบ User API หลังจากสร้าง endpoint แล้ว

ไฟล์ที่ใช้:

- `requests.http`: ชุด request สำหรับ IntelliJ HTTP Client หรือใช้เป็นต้นแบบใน Postman

## วิธีใช้กับ IntelliJ IDEA

เปิดไฟล์ `requests.http` แล้วกดปุ่ม run ข้าง request ที่ต้องการทดสอบ

## วิธีใช้กับ Postman

สร้าง environment variable:

| Variable  | Initial value           |
| --------- | ----------------------- |
| `baseUrl` | `http://localhost:8080` |

จากนั้นสร้าง request ตามไฟล์ `requests.http`

## ลำดับทดสอบที่แนะนำ

1. `GET /api/v1/users` ต้องได้ `[]`
2. `POST /api/v1/users` ต้องได้ user พร้อม `id`
3. `GET /api/v1/users/1` ต้องได้ user ที่สร้าง
4. `DELETE /api/v1/users/1` ต้องได้ status `204 No Content`
5. `GET /api/v1/users/1` อีกครั้งต้องได้ status `404 Not Found`
