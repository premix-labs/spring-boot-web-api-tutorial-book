---
title: "0002 - Progressive and Final Projects"
description: เหตุผลในการมีทั้ง examples รายบทและ final project
---

# 0002 - Progressive and Final Projects

## Status

Accepted

## Decision

repo ต้องมีสองระดับตัวอย่าง:

- `examples/chapter-*` สำหรับ code shape ของบทนั้น
- `examples/final-backend-api` สำหรับ end-state ที่รวมระบบครบ

## Context

หนังสือที่สอนทีละบทต้องค่อย ๆ เพิ่ม concept แต่ final reference ควรแสดงโครงที่พร้อมต่อยอดจริงกว่า

## Rules

- chapter content ต้องตรงกับ chapter example ณ จุดนั้น
- final project ใช้เป็น source of truth สำหรับ end-state
- ความต่างระหว่าง chapter example และ final project ต้องอธิบายในบทที่เกี่ยวข้อง
- validation report ต้องบันทึกผลทำตามจริง

