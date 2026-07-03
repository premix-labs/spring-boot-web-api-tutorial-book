---
title: หลักการสอน
description: มาตรฐานการเขียนบทเรียน Spring Boot ให้ทำตามได้จริง
---

# หลักการสอน

หนังสือเล่มนี้ต้องสอนให้ผู้เรียนสร้าง backend API ที่รันได้จริง โดยเข้าใจเหตุผลของโค้ด ไม่ใช่แค่พิมพ์ตาม

## Definition of Done ของแต่ละบท

บทหนึ่งจะถือว่าพร้อมสอนเมื่อมีครบ:

- เป้าหมายของบท
- prerequisites
- ไฟล์ที่จะสร้างหรือแก้
- concept ใหม่ก่อนใช้
- คำสั่งที่ copy ได้
- code ทีละส่วน
- expected result
- common errors
- checkpoint

## ลำดับการสอนโค้ด

สอนเป็นชั้น:

1. สร้าง package/folder/file
2. เพิ่ม import
3. เพิ่ม class, record หรือ interface
4. เพิ่ม field/property
5. เพิ่ม constructor หรือ dependency
6. เพิ่ม method ทีละ method
7. ต่อ annotation/configuration
8. ต่อ controller/route
9. ทดสอบผล

## Spring Boot Rules

- Controller ควรรับ HTTP request และส่ง response
- Service ควรเก็บ business logic
- Repository ควรซ่อน data access ผ่าน Spring Data JPA
- DTO ควรเป็น API contract ไม่ใช่ entity ภายใน
- `@Configuration`, `@Bean`, `@Service`, `@Repository`, `@RestController` ต้องอธิบายก่อนใช้
- Gradle dependency ต้องตรงกับ Spring Boot version
- บท production-ready ต้องบอกข้อจำกัดและความเสี่ยง

## Progressive Path

ต้องรักษาลำดับการเรียน:

- บทต้นไม่ควรใช้ field หรือ class จากบทท้าย
- ถ้า final project ต่างจาก chapter example ต้องอธิบายเหตุผล
- examples รายบทคือหลักฐานว่า snippet ทำงานได้
- final project คือ reference ของ end-state

## Verification Gate

ก่อนถือว่าบทพร้อม ต้องรันคำสั่งตาม scope:

```powershell
npm run build
```

ถ้าแตะ final project:

```powershell
cd examples/final-backend-api
.\gradlew.bat clean test bootJar
```

ถ้าแตะ Docker:

```powershell
docker compose config
```

