---
title: 34 - Database Migration ด้วย Flyway
description: เปลี่ยนจาก ddl-auto=update ไปใช้ SQL migration ที่ควบคุม version ได้
---

## เป้าหมายของบท

บทนี้จะเปลี่ยนวิธีจัดการ schema จาก Hibernate `ddl-auto=update` ไปใช้ Flyway migration

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- ทำไม `ddl-auto=update` ไม่เหมาะกับ production
- Flyway migration file ตั้งชื่ออย่างไร
- เก็บ migration ไว้ที่ไหน
- เปลี่ยน `ddl-auto` เป็น `validate` อย่างไร
- migration table `flyway_schema_history` ใช้ทำอะไร

## ปัญหาของ ddl-auto=update

ตอนเรียนเราใช้:

```properties
spring.jpa.hibernate.ddl-auto=update
```

เพราะสะดวก แต่ production ไม่ควรใช้ เพราะ:

- schema เปลี่ยนอัตโนมัติโดยไม่มี review
- rollback ยาก
- ไม่เห็น history ชัดเจนว่า table เปลี่ยนเมื่อไร
- deploy หลาย environment แล้วเสี่ยง schema ไม่ตรงกัน

งานจริงควรใช้ migration script ที่ถูก commit เข้า Git

## เพิ่ม dependency

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-flyway'
    implementation 'org.flywaydb:flyway-database-postgresql'
}
```

สำหรับ Spring Boot 4 ให้ใช้ `spring-boot-starter-flyway` เพื่อให้ auto-configuration ของ Flyway ถูกโหลดด้วย
ถ้าใส่แค่ `flyway-core` migration จะไม่ถูกรันตอน start application

## ตั้งค่า application.properties

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

`validate` หมายถึง Hibernate จะตรวจว่า entity กับ table เข้ากันได้ แต่จะไม่สร้าง table เอง

## สร้าง migration แรก

สร้างไฟล์:

```text
src/main/resources/db/migration/V1__create_users_table.sql
```

ตัวอย่าง:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

สร้าง audit logs:

```text
src/main/resources/db/migration/V2__create_audit_logs_table.sql
```

```sql
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    actor_id BIGINT NOT NULL,
    target_user_id BIGINT NOT NULL,
    old_value VARCHAR(100),
    new_value VARCHAR(100),
    created_at TIMESTAMP NOT NULL
);
```

## Naming convention

รูปแบบ:

```text
V<version>__<description>.sql
```

ตัวอย่าง:

```text
V1__create_users_table.sql
V2__create_audit_logs_table.sql
V3__add_last_login_at_to_users.sql
```

ต้องมี underscore 2 ตัวระหว่าง version กับ description:

```text
V1__create_users_table.sql
```

ไม่ใช่:

```text
V1_create_users_table.sql
```

## flyway_schema_history

เมื่อรัน application Flyway จะสร้าง table:

```text
flyway_schema_history
```

table นี้เก็บว่า migration ไหนถูกรันแล้ว ถ้า `V1` รันไปแล้ว Flyway จะไม่รันซ้ำ

## เปลี่ยน schema ในอนาคต

ห้ามแก้ migration เก่าที่รันไปแล้วใน environment อื่น

ถ้าต้องเพิ่ม column ให้สร้างไฟล์ใหม่:

```text
V3__add_last_login_at_to_users.sql
```

```sql
ALTER TABLE users
ADD COLUMN last_login_at TIMESTAMP;
```

## แบบฝึกหัดท้ายบท

1. เพิ่ม Flyway dependency
2. เปลี่ยน `ddl-auto` เป็น `validate`
3. สร้าง `V1__create_users_table.sql`
4. สร้าง `V2__create_audit_logs_table.sql`
5. รัน application แล้วตรวจ `flyway_schema_history`
6. เพิ่ม migration ใหม่เพื่อเพิ่ม column `last_login_at`
