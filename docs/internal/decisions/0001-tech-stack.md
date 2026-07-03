---
title: "0001 - Tech Stack"
description: เหตุผลในการเลือก stack หลักของหนังสือ
---

# 0001 - Tech Stack

## Status

Accepted

## Decision

หนังสือใช้ stack หลัก:

- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Spring Security + JWT
- Flyway
- JUnit/MockMvc integration tests
- Docker Compose
- Astro/Starlight สำหรับเว็บไซต์หนังสือ

## Context

เป้าหมายคือสอน backend API ที่ใกล้งานจริง มี database, authentication, admin, tests และ deployment path โดยยังให้มือใหม่ทำตามได้ทีละบท

## Consequences

ข้อดี:

- stack สอดคล้องกับ Java backend ที่ใช้จริง
- Spring Web MVC เหมาะกับการสอน controller/service/repository
- JPA + PostgreSQL ทำ database flow ชัด
- Flyway สอน migration แบบ production-ready

ข้อเสีย:

- setup หนักกว่า in-memory tutorial
- PostgreSQL ต้องใช้ local install หรือ Docker
- Spring Security/JWT ต้องสอนอย่างระวังไม่ให้กระโดดเร็วเกินไป

