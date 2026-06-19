# Production Checklist

- `./gradlew clean build` ผ่าน
- test ผ่าน
- production ใช้ environment variables
- ไม่มี password หรือ secret จริงใน Git
- Flyway migration ผ่าน
- `spring.jpa.hibernate.ddl-auto=validate`
- JWT secret เปลี่ยนจากค่า demo แล้ว
- log ไม่มี password หรือ token เต็ม
- `/actuator/health` ตอบ `UP`
- database backup/restore มีแผน
- admin endpoint ต้องใช้ role `ADMIN`
- Swagger UI เปิดหรือปิดตาม policy ของทีม

