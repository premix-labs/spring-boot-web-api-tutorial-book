---
title: GitHub Pages Deploy
description: วิธี deploy เว็บหนังสือ Astro/Starlight ขึ้น GitHub Pages
---
หน้านี้สรุปวิธี deploy หนังสือขึ้น GitHub Pages ด้วย GitHub Actions

## สิ่งที่เตรียมไว้แล้ว

โปรเจกต์นี้เตรียมไฟล์สำหรับ deploy แล้ว:

```text
.github/workflows/deploy.yml
astro.config.mjs
public/.nojekyll
```

`astro.config.mjs` อ่านค่า environment variables:

```text
SITE
BASE_PATH
```

workflow จะตั้งค่าสองตัวนี้ให้อัตโนมัติตามชื่อเจ้าของ repo และชื่อ repo

## รูปแบบ URL ของ GitHub Pages

ถ้า repo ชื่อ `spring-boot-tutorial-book` URL จะเป็น:

```text
https://<username>.github.io/spring-boot-tutorial-book/
```

ถ้า repo เป็น user site เช่น `<username>.github.io` URL จะเป็น:

```text
https://<username>.github.io/
```

## ขั้นตอน deploy

1. สร้าง GitHub repository ใหม่
2. ใช้โฟลเดอร์ `spring-boot-tutorial-book` เป็น root ของ repo
3. push ขึ้น branch `main`
4. เปิดหน้า repository บน GitHub
5. ไปที่ `Settings` → `Pages`
6. เลือก `Source: GitHub Actions`
7. ไปที่แท็บ `Actions`
8. เปิด workflow `Deploy to GitHub Pages`
9. กด `Run workflow` หรือ push commit ใหม่เข้า branch `main`

หลัง workflow สำเร็จ GitHub จะแสดง URL ของเว็บในหน้า deploy

## ตรวจ build ก่อน push

ก่อน push ควรรัน:

```powershell
npm install
npm run build
```

ถ้าต้องการจำลอง base path ของ GitHub Pages ในเครื่อง:

```powershell
$env:SITE="https://example.github.io"
$env:BASE_PATH="/spring-boot-tutorial-book"
npm run build
```

หลังทดสอบเสร็จ ถ้าต้องการกลับเป็นค่า local:

```powershell
Remove-Item Env:SITE
Remove-Item Env:BASE_PATH
```

## กรณีใช้ custom domain

ถ้าใช้ custom domain เช่น `book.example.com` ให้ทำเพิ่ม:

1. ตั้งค่า custom domain ใน GitHub Pages settings
2. เพิ่มไฟล์ `public/CNAME`
3. ใส่ domain จริงในไฟล์นั้น เช่น:

```text
book.example.com
```

จากนั้นปรับ `SITE` ใน workflow ให้เป็น:

```text
https://book.example.com
```

และไม่ต้องตั้ง `BASE_PATH`

## ปัญหาที่พบบ่อย

- หน้าเว็บขึ้น แต่ CSS/JS หาย: มักเกิดจาก `base` ไม่ตรงกับชื่อ repo
- เปิด URL แล้ว 404: ตรวจว่า Pages settings เลือก `GitHub Actions`
- workflow ไม่ทำงาน: ตรวจว่าไฟล์ `.github/workflows/deploy.yml` อยู่ที่ root ของ repo
- repository ไม่ใช่ branch `main`: แก้ workflow ให้ trigger branch ที่ใช้จริง
