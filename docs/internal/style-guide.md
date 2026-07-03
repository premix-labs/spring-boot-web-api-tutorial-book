---
title: Style Guide
description: มาตรฐานภาษา รูปแบบโค้ด และรูปแบบเอกสาร
---

# Style Guide

ใช้เอกสารนี้เพื่อให้หนังสือทั้งเล่มมีเสียงเดียวกันและอ่านตามได้จริง

## ภาษาและคำศัพท์

- ใช้ภาษาไทยเป็นหลัก
- คำเทคนิคที่ผู้อ่านจะเจอใน code ให้คงอังกฤษ เช่น Controller, Service, Repository, DTO, Entity, JWT, Flyway
- ถ้าคำใหม่สำคัญ ให้แปลความหมายก่อนใช้ใน code
- หลีกเลี่ยงคำกว้าง ๆ เช่น "เพิ่มโค้ดนี้" โดยไม่บอกตำแหน่ง
- ระบุระดับของตัวอย่างให้ชัดเมื่อเป็น demo, portfolio-ready หรือ production-oriented

## ชื่อไฟล์และ Path

ใช้ path ที่สัมพันธ์กับ root ของโปรเจกต์ตัวอย่าง:

```text
src/main/java/com/example/backendapi/controller/UserController.java
src/main/java/com/example/backendapi/service/UserService.java
src/main/resources/application.properties
```

## Code Blocks

- ใส่ language tag เช่น `java`, `properties`, `sql`, `json`, `powershell`
- ไม่ใช้ code block ยาวเกินประมาณ 30 บรรทัดถ้าไม่จำเป็น
- ถ้าต้องแสดงไฟล์เต็ม ให้แบ่ง section และบอกว่าเป็น final shape
- ห้ามใช้ placeholder ที่ทำให้ copy แล้ว compile ไม่ผ่านโดยไม่อธิบาย

## Commands

Windows PowerShell:

```powershell
.\gradlew.bat test
```

macOS/Linux Bash:

```bash
./gradlew test
```

## API Examples

ทุก endpoint สำคัญควรมี:

- method และ route
- request body ถ้ามี
- status code สำเร็จ
- response body สำเร็จ
- error status สำคัญ เช่น `400`, `401`, `403`, `404`, `409`

## Security Language

เมื่อสอน auth, token, password, CORS หรือ secret ต้อง:

- บอกข้อจำกัดของ demo
- ห้ามสื่อว่า secret ใน `application.properties` เป็น production best practice
- แยก local development, test และ production configuration
- อธิบายว่า role/permission ต้อง enforce ที่ backend

