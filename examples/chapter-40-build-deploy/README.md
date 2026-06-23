# Chapter 40 - Build และ Deploy

ตัวอย่างนี้ใช้ประกอบบทที่ 40 สำหรับ build, run และตรวจ checklist ก่อน deploy

ไฟล์ที่ใช้:

- `build-commands.ps1`
- `production-checklist.md`
- `application-actuator.properties`

## Build

```powershell
.\gradlew.bat clean build
```

## Run

```powershell
java -jar build/libs/backend-api-0.0.1-SNAPSHOT.jar
```

