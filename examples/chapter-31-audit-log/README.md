# Chapter 31 - Audit Log เบื้องต้น

ตัวอย่างนี้ใช้ประกอบบทที่ 31 สำหรับบันทึก action สำคัญของ admin

ไฟล์ที่ใช้:

- `AuditLog.java`
- `AuditLogRepository.java`
- `AuditLogResponse.java`
- `AuditLogService.java`
- `AdminUserController.java`
- `AdminUserService.java`
- `AdminAuditLogController.java`
- `requests.http`

Audit log จะถูกบันทึกเมื่อ admin เปลี่ยน role หรือ status ของ user
