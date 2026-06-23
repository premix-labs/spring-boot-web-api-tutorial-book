# Chapter 05 - First Controller

ตัวอย่างนี้ใช้กับบท `05 - Controller แรก`

## ไฟล์ที่ต้องสร้างใน Spring Boot project

```text
src/main/java/com/example/backendapi/controller/HelloController.java
```

## Endpoint ที่ได้

```text
GET /hello
GET /health
GET /version
```

## วิธีทดสอบ

รัน application:

```powershell
.\gradlew.bat bootRun
```

ทดสอบด้วย browser หรือ Postman:

```text
http://localhost:8080/hello
http://localhost:8080/health
http://localhost:8080/version
```

## Expected Response

```text
GET /hello   -> Hello Spring Boot
GET /health  -> OK
GET /version -> Backend API v1
```
