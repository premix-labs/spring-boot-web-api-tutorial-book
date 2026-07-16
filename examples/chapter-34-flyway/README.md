# Chapter 34 - Database Migration ด้วย Flyway

ตัวอย่างนี้ใช้ประกอบบทที่ 34 สำหรับเปลี่ยนจาก `ddl-auto=update` ไปใช้ Flyway

ไฟล์ที่ใช้:

- `build.gradle-snippet.gradle`
- `application.properties`
- `V1__create_users_table.sql`
- `V2__create_audit_logs_table.sql`

ให้นำไฟล์ SQL ไปวางที่:

```text
src/main/resources/db/migration/
```
