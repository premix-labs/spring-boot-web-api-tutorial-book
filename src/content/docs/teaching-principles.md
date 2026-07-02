---
title: แนวทางหนังสือสอนเขียนเว็บที่ดี
description: สรุปมาตรฐานการสอนและตำแหน่ง Codex skill สำหรับตรวจหนังสือทีละบท
---

หน้านี้เป็นสรุปสำหรับผู้อ่านและผู้เขียนหนังสือ ส่วน checklist ฉบับเต็มสำหรับให้ Codex ใช้ตรวจทีละบทอยู่ใน `skills/tutorial-book-auditor/references/teaching-principles.md`

## หลักการ

หนังสือสอนเขียนเว็บที่ดีต้องทำให้ผู้เรียนรู้ว่า:

- กำลังสร้างอะไร
- ทำไมต้องเขียนโค้ดส่วนนั้น
- ต้องสร้างไฟล์หรือโฟลเดอร์ไหนก่อน
- ต้องรันคำสั่งอะไรเพื่อตรวจผล
- ผลลัพธ์ที่ถูกต้องควรหน้าตาเป็นอย่างไร
- ถ้ารันแล้วไม่ตรง ควรตรวจจุดไหนก่อน

## มาตรฐานของบทที่ดี

ทุกบทควรมีเป้าหมายชัด อธิบายคำใหม่ก่อนใช้ แบ่งโค้ดเป็นขั้นเล็ก ๆ มีคำสั่งที่ copy ได้ มี expected result และมี checkpoint ที่ผู้เรียนตรวจเองได้

ถ้าบทนั้นสร้างไฟล์หรือโฟลเดอร์ใหม่ ควรมีคำสั่งทั้ง Windows PowerShell และ macOS/Linux Bash เมื่อคำสั่งต่างกัน

ถ้าบทนั้นใช้ concept ใหม่ เช่น Controller, Service, Repository, DTO, Validation, Authentication, Docker หรือ Deployment ต้องอธิบายก่อนว่า concept นั้นแก้ปัญหาอะไร และตอนนี้เราใช้เฉพาะส่วนไหน

## ใช้กับ Codex อย่างไร

repo นี้มี project-local skill ที่ `skills/tutorial-book-auditor`

เวลาให้ Codex ตรวจหรือปรับบทเรียน ให้สั่งประมาณนี้:

```text
ใช้ tutorial-book-auditor ตรวจ Spring Boot part 4 บทที่ 16 ให้ที
```

Codex ควรอ่าน `AGENTS.md`, `skills/tutorial-book-auditor/SKILL.md` และ reference ของ skill ก่อนตรวจหรือแก้เนื้อหา

## เป้าหมาย

เป้าหมายสุดท้ายคือหนังสือที่ผู้เรียนทำตามได้จริง เข้าใจเหตุผล ตรวจงานตัวเองได้ และเห็นชัดว่าตัวอย่างในหนังสืออยู่ระดับ demo, portfolio-ready หรือ production-grade
