---
title: '0003 - Auth and Security Model'
description: เหตุผลของแนวทาง auth และ security ในหนังสือ
---

# 0003 - Auth and Security Model

## Status

Accepted

## Decision

หนังสือใช้ Spring Security + JWT สำหรับ authentication และ role-based authorization

ระบบหลัก:

- BCrypt password hashing
- login endpoint ที่ออก JWT
- current user endpoint
- role claim
- admin-only endpoint
- audit log สำหรับ admin action

## Context

ระบบ admin ต้องมี auth behavior ที่จริงจังพอให้ผู้เรียนเห็นความต่างระหว่าง public API, authenticated API และ admin API

## Consequences

ข้อดี:

- ต่อ frontend admin dashboard ได้จริง
- สอน security boundary ได้ชัด
- final project เป็น reference ที่น่าเชื่อถือ

ข้อเสีย:

- Spring Security มี concept เยอะ ต้องสอนทีละชั้น
- ต้องระวังไม่ให้บทต้นโดน security filter ขวางก่อนถึงบท auth
