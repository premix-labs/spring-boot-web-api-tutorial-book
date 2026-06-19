---
title: สถานะต้นฉบับ
description: สถานะงานเขียนและสิ่งที่ต้องทำต่อของหนังสือ
---
หนังสือเล่มนี้อยู่ในสถานะ `v1.0 Complete Book Draft`

หมายความว่า:

- มีโครงสร้างครบทั้งเล่ม
- มีหน้าแรกและแผนทั้งเล่ม
- มีเนื้อหาครบ 7 ภาค
- มีบทหลักครบ 40 บท
- มีภาคผนวกสำหรับเปิดดูเร็ว
- ทุกบทมีเนื้อหาพื้นฐาน, ตัวอย่างโค้ด หรือ checklist สำหรับเขียนต่อ
- บทที่ 1-5 ถูกขยายเป็น draft ที่อ่านตามได้จริงแล้ว
- บทที่ 6-10 ถูกขยายเป็น draft สำหรับ REST API ที่อ่านและทำตามได้จริงแล้ว
- บทที่ 11-15 ถูกขยายเป็น draft สำหรับ PostgreSQL, JPA, Repository และ CRUD ที่อ่านตามได้จริงแล้ว
- บทที่ 16-20 ถูกขยายเป็น draft สำหรับ validation, exception handling, DTO, response format และ pagination ที่อ่านตามได้จริงแล้ว
- บทที่ 21-26 ถูกขยายเป็น draft สำหรับ register, password hashing, login, JWT, current user และ protected endpoints ที่อ่านตามได้จริงแล้ว
- บทที่ 27-31 ถูกขยายเป็น draft สำหรับ role, admin-only endpoints, admin user list, change role/status และ audit log ที่อ่านตามได้จริงแล้ว
- บทที่ 32-40 ถูกขยายเป็น draft สำหรับ logging, transaction, Flyway, profiles/env, OpenAPI, testing, Docker Compose และ build/deploy ที่อ่านตามได้จริงแล้ว
- มีตัวอย่างโค้ด controller แรกใน `examples/chapter-05-first-controller/`
- มีตัวอย่างโค้ด User API แบบ in-memory ใน `examples/chapter-07-user-api-in-memory/`
- มีตัวอย่างโค้ดการแยก layer ใน `examples/chapter-08-layered-architecture/`
- มีชุด request สำหรับทดสอบ API ใน `examples/chapter-09-postman-testing/`
- มีตัวอย่าง config PostgreSQL ใน `examples/chapter-11-postgresql-config/`
- มีตัวอย่าง entity/repository ใน `examples/chapter-12-jpa-entity/` และ `examples/chapter-13-jpa-repository/`
- มีตัวอย่าง User CRUD กับ database ใน `examples/chapter-14-user-crud-database/`
- มีตัวอย่าง update endpoint ใน `examples/chapter-15-update-user/`
- มีตัวอย่าง validation ใน `examples/chapter-16-validation/`
- มีตัวอย่าง global exception handler ใน `examples/chapter-17-exception-handling/`
- มีตัวอย่าง DTO ใน `examples/chapter-18-dto/`
- มีตัวอย่าง response wrapper ใน `examples/chapter-19-response-format/`
- มีตัวอย่าง pagination, sorting และ filtering ใน `examples/chapter-20-pagination-sorting-filtering/`
- มีตัวอย่าง register ใน `examples/chapter-21-register-design/`
- มีตัวอย่าง password hashing ใน `examples/chapter-22-password-hashing/`
- มีตัวอย่าง login ใน `examples/chapter-23-login/`
- มีตัวอย่าง JWT service และ resource server config ใน `examples/chapter-24-jwt-token/`
- มีตัวอย่าง current user endpoint ใน `examples/chapter-25-current-user/`
- มีตัวอย่าง protected/admin endpoints ใน `examples/chapter-26-protect-endpoints/`
- มีตัวอย่าง role/status และ JWT role claim ใน `examples/chapter-27-roles/`
- มีตัวอย่าง admin-only endpoint ใน `examples/chapter-28-admin-only-endpoints/`
- มีตัวอย่าง admin user list ใน `examples/chapter-29-admin-user-list/`
- มีตัวอย่าง change role/status ใน `examples/chapter-30-change-role-status/`
- มีตัวอย่าง audit log ใน `examples/chapter-31-audit-log/`
- มีตัวอย่าง logging ใน `examples/chapter-32-logging/`
- มีตัวอย่าง transaction ใน `examples/chapter-33-transaction/`
- มีตัวอย่าง Flyway migration ใน `examples/chapter-34-flyway/`
- มีตัวอย่าง profiles/env ใน `examples/chapter-35-profiles-env/`
- มีตัวอย่าง OpenAPI/Swagger ใน `examples/chapter-36-openapi-swagger/`
- มีตัวอย่าง unit test ใน `examples/chapter-37-unit-test/`
- มีตัวอย่าง integration test ใน `examples/chapter-38-integration-test/`
- มีตัวอย่าง Docker Compose ใน `examples/chapter-39-docker-compose/`
- มีตัวอย่าง build/deploy checklist ใน `examples/chapter-40-build-deploy/`
- มีโปรเจกต์รวมท้ายเล่มใน `examples/final-secure-admin-api/`
- โปรเจกต์รวมท้ายเล่มรัน `test` และ `bootJar` ผ่านแล้วด้วย JDK 25
- มีหน้าอธิบาย final project ใน `appendix/final-project`
- มี Postman collection และ environment สำหรับ final project ใน `examples/final-secure-admin-api/postman/`
- มี GitHub Actions workflow สำหรับ deploy เว็บหนังสือขึ้น GitHub Pages
- มีหน้าอธิบายขั้นตอน deploy ใน `appendix/github-pages-deploy`

## สิ่งที่ยังควรทำก่อนเผยแพร่จริง

รอบถัดไปก่อนเผยแพร่จริงควรทำตามลำดับนี้:

1. ตรวจเทียบ code snippet รายบทกับ `final-secure-admin-api` อีกรอบ เพื่อให้ชื่อ class/package ตรงกันมากที่สุด
2. เพิ่มภาพ diagram เช่น request flow, layered architecture, JPA flow, JWT flow, admin flow, deployment flow
3. ตรวจภาษาไทย ความลื่น และความสม่ำเสมอของคำศัพท์ทั้งเล่ม
4. ตรวจลิขสิทธิ์ dependency/version และ pin version ที่ใช้ในหนังสือ

## Definition of Done สำหรับแต่ละบท

บทหนึ่งบทจะถือว่าพร้อมเผยแพร่เมื่อมีครบ:

- เป้าหมายของบท
- ภาพรวมแนวคิด
- ขั้นตอนลงมือทำ
- code snippet ที่ทดสอบแล้ว
- วิธีทดสอบด้วย Postman หรือ command line
- ปัญหาที่พบบ่อย
- แบบฝึกหัดท้ายบท

## แนวทางเขียนต่อ

ให้เขียนแบบ "รันได้ก่อน แล้วค่อยอธิบาย" เพราะกลุ่มเป้าหมายเป็นมือใหม่ Spring Boot การเห็นผลลัพธ์เร็วจะช่วยให้ผู้อ่านไม่หลุดตั้งแต่ช่วง setup
