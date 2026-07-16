---
title: แผนหนังสือ Spring Boot Web API
description: สารบัญ ขอบเขต และผลลัพธ์สุดท้ายของหนังสือ Spring Boot Web API
---

# แผนหนังสือ Spring Boot Web API

หนังสือเล่มนี้สอนสร้าง backend API ด้วย Spring Boot จากพื้นฐานไปถึงระบบที่มี database, validation, authentication, admin, testing, Docker และ production-ready workflow

## กลุ่มผู้อ่าน

- ผู้เริ่มต้น Spring Boot REST API
- ผู้ที่รู้ Java พื้นฐานและอยากทำ backend API จริง
- ผู้ที่ต้องการ portfolio backend project ที่มี auth, admin และ database
- ผู้ที่อยากเข้าใจ flow แบบ Controller, Service, Repository, DTO และ JPA

## ผลลัพธ์สุดท้าย

ผู้เรียนควรได้ backend API ที่มี:

- REST CRUD API
- PostgreSQL + Spring Data JPA
- validation และ global exception handling
- DTO และ response format
- register/login
- BCrypt password hashing
- JWT authentication
- current user endpoint
- role-based authorization
- admin user management
- audit log
- pagination/filtering/sorting
- unit และ integration tests
- Flyway migration
- Docker Compose
- OpenAPI/Swagger
- production-ready build/deploy checklist

## Part 1: Foundation

1. Spring Boot คืออะไร
2. ติดตั้งเครื่องมือ
3. สร้างโปรเจกต์แรก
4. เข้าใจ project structure

## Part 2: REST API

5. สร้าง Controller แรก
6. HTTP และ REST API
7. User API แบบ in-memory
8. Layered architecture
9. ทดสอบด้วย Postman หรือ REST Client
10. REST API review

## Part 3: Database

11. PostgreSQL config
12. JPA entity
13. JPA repository
14. User CRUD กับ database
15. Update user

## Part 4: Better API

16. Validation
17. Exception handling
18. DTO
19. Response format
20. Pagination, sorting และ filtering

## Part 5: Authentication

21. Register design
22. Password hashing
23. Login
24. JWT token
25. Current user
26. Protect endpoints

## Part 6: Admin

27. Roles
28. Admin-only endpoints
29. Admin user list
30. Change role/status
31. Audit log

## Part 7: Production Ready

32. Logging
33. Transaction
34. Flyway
35. Profiles and environment variables
36. OpenAPI/Swagger
37. Unit test
38. Integration test
39. Docker Compose
40. Build and deploy
