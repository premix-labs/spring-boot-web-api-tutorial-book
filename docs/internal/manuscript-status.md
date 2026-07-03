---
title: สถานะต้นฉบับ
description: สถานะการเขียนและตรวจสอบเนื้อหาของหนังสือ Spring Boot Web API
---

# สถานะต้นฉบับ

หน้านี้ใช้ติดตามว่าส่วนไหนของหนังสือพร้อมแล้ว ส่วนไหนยังต้องเขียนหรือขัดเกลา และส่วนไหนตรวจด้วยโปรเจกต์จริงแล้ว

## สถานะปัจจุบัน

- โครงเว็บไซต์หนังสือ: เสร็จแล้ว
- สารบัญ 40 บท: เสร็จแล้ว
- เอกสาร product-ready internal docs: เสร็จแล้ว
- API contract กลาง: ร่างแรก
- โครง final project: ร่างแรก
- teaching principles: เสร็จแล้ว
- style guide: เสร็จแล้ว
- release checklist: เสร็จแล้ว
- technical decision records: เสร็จร่างแรก
- QA checklist: เสร็จร่างแรก
- ภาค 1-7: เขียนครบ draft แล้ว
- Final project: สร้างแล้ว
- Validation report เดิม: เก็บไว้ที่ root เป็น legacy summary
- GitHub Pages workflow: มีแล้ว

## เอกสารควบคุมคุณภาพ

เอกสารที่ต้องอ่านก่อนเขียนหรือแก้บท:

- `docs/internal/book-plan.md`
- `docs/internal/api-contract.md`
- `docs/internal/teaching-principles.md`
- `docs/internal/style-guide.md`
- `docs/internal/final-project-structure.md`

เอกสารที่ต้องใช้ก่อน release:

- `docs/internal/release-checklist.md`
- `docs/internal/validation-report.md`
- `docs/internal/qa/browser-test-plan.md`
- `docs/internal/qa/accessibility-checklist.md`
- `docs/internal/qa/security-review-checklist.md`

เอกสาร decision records:

- `docs/internal/decisions/0001-tech-stack.md`
- `docs/internal/decisions/0002-progressive-and-final-projects.md`
- `docs/internal/decisions/0003-auth-security-model.md`
- `docs/internal/decisions/0004-gradle-and-versioning.md`
- `docs/internal/decisions/0005-deployment-target.md`

## งานถัดไป

1. ตรวจว่าเอกสาร internal ใหม่สะท้อน final project ปัจจุบันครบ
2. ตรวจเทียบ chapter snippets กับ final project และ examples รายบท
3. ถ้าจะ publish รอบใหม่ ให้รัน release checklist
4. เปิดเว็บจริงและตรวจ browser smoke test

## Definition of Done

หนังสือจะถือว่าพร้อมเผยแพร่เมื่อ:

- ทุกบทมี goal, steps, code, expected result และ checkpoint
- examples รายบทตรงกับบทที่เกี่ยวข้อง
- final project build/test/bootJar ผ่าน
- Docker Compose config ผ่าน
- secret hygiene ผ่าน
- เว็บไซต์ build ผ่าน
- browser QA ผ่าน

