---
title: '0004 - Gradle and Versioning'
description: เหตุผลในการใช้ Gradle wrapper และคุม dependency version
---

# 0004 - Gradle and Versioning

## Status

Accepted

## Decision

ใช้ Gradle wrapper จาก project และให้คำสั่งในหนังสือใช้ wrapper เสมอ:

```powershell
.\gradlew.bat test
```

```bash
./gradlew test
```

## Context

ผู้เรียนอาจมี Gradle global version ต่างกัน การใช้ wrapper ทำให้ build reproducible กว่า

## Consequences

ข้อดี:

- ลดปัญหา version mismatch
- รันได้เหมือนกันใน CI และเครื่อง local
- เหมาะกับหนังสือที่ผู้เรียนใช้ environment ต่างกัน

ข้อเสีย:

- ต้องอธิบายความต่างระหว่าง Gradle global กับ wrapper
- ต้องมีคำสั่งแยก Windows และ macOS/Linux
