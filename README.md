# Spring Boot Tutorial Book

หนังสือสอน Spring Boot สำหรับมือใหม่ โดยใช้โปรเจกต์ `Backend API` เป็นแกนหลักในการเรียนรู้ตั้งแต่พื้นฐานจนถึง production-ready backend API

เนื้อหาหลัก:

- Spring Boot REST API
- Layered Architecture
- PostgreSQL + Spring Data JPA
- Validation, Exception Handling และ DTO
- Register/Login
- BCrypt password hashing
- JWT Authentication
- User และ Admin API
- Testing, Docker, Swagger และแนวทาง deploy

## สถานะ

โปรเจกต์นี้อยู่ในสถานะ `v1.0 Complete Book Draft` และพร้อม deploy เป็นเว็บหนังสือด้วย GitHub Pages แล้ว

สิ่งที่มีแล้ว:

- เนื้อหาหนังสือครบ 40 บท
- ภาคผนวกสำหรับเปิดดูเร็ว
- โค้ดตัวอย่างรายบทใน `examples/`
- โปรเจกต์รวมท้ายเล่มใน `examples/final-backend-api/`
- Postman collection สำหรับ final project
- GitHub Actions workflow สำหรับ deploy ไป GitHub Pages

## Project Structure

```text
.
├── .github/workflows/deploy.yml
├── docs/internal/    # เอกสารควบคุมคุณภาพสำหรับพัฒนา/release หนังสือ
├── public/
├── src/
│   ├── assets/
│   ├── content/
│   │   └── docs/       # เนื้อหาหนังสือแต่ละบท
│   └── content.config.ts
├── examples/           # โค้ดตัวอย่างประกอบหนังสือ
├── astro.config.mjs
├── package.json
└── tsconfig.json
```

## เอกสารควบคุมคุณภาพ

เอกสารภายในสำหรับพัฒนา ตรวจ และ release หนังสืออยู่ที่ `docs/internal` และใช้ `Book Documentation Standard v1` เหมือนหนังสือเล่มอื่น อ่านมาตรฐานได้ที่ `docs/internal/README.md`

```text
docs/internal/
  README.md
  book-plan.md
  api-contract.md
  final-project-structure.md
  manuscript-status.md
  release-checklist.md
  style-guide.md
  teaching-principles.md
  validation-report.md
  decisions/
  qa/
```

ก่อนแก้บทเรียนหรือ example project ให้อ่าน:

- `AGENTS.md`
- `skills/tutorial-book-auditor/SKILL.md`
- `skills/tutorial-book-auditor/references/teaching-principles.md`
- `docs/internal/teaching-principles.md`
- `docs/internal/style-guide.md`

ก่อน release ให้อ่าน:

- `docs/internal/release-checklist.md`
- `docs/internal/validation-report.md`
- `docs/internal/qa/browser-test-plan.md`
- `docs/internal/qa/accessibility-checklist.md`
- `docs/internal/qa/security-review-checklist.md`

## Local Commands

```powershell
npm install
npm run dev
npm run build
npm run preview
```

คำสั่งที่ควรรันก่อน push:

```powershell
npm run build
```

## GitHub Pages Deploy

ให้ใช้โฟลเดอร์ `spring-boot-tutorial-book` เป็น root ของ GitHub repository

ขั้นตอน:

1. สร้าง GitHub repository ใหม่
2. push โฟลเดอร์นี้ขึ้น branch `main`
3. ไปที่ `Settings` → `Pages`
4. ใน `Build and deployment` เลือก `Source: GitHub Actions`
5. push หรือกด `Run workflow` ที่ workflow ชื่อ `Deploy to GitHub Pages`

workflow จะตั้งค่า URL ให้อัตโนมัติ:

- repository ปกติ: `https://<username>.github.io/<repo-name>/`
- repository แบบ user/organization site: `https://<username>.github.io/`

ไฟล์สำคัญสำหรับ deploy:

- `.github/workflows/deploy.yml`
- `astro.config.mjs`
- `public/.nojekyll`

## GitHub Pages Build Variables

`astro.config.mjs` อ่าน environment variables เหล่านี้:

```text
SITE      = https://<username>.github.io
BASE_PATH = /<repo-name>
```

ใน GitHub Actions ตั้งค่าให้อัตโนมัติแล้ว ปกติไม่ต้องแก้เอง

ถ้าใช้ custom domain ให้ปรับ `SITE` และเอา `BASE_PATH` ออกตามโดเมนจริง

## Writing Flow

1. เขียนหรือแก้เนื้อหาใน `src/content/docs/`
2. เก็บโค้ดประกอบบทไว้ใน `examples/`
3. รัน `npm run dev` เพื่อ preview
4. รัน `npm run build` ก่อน publish
5. push ขึ้น GitHub เพื่อ deploy ผ่าน GitHub Actions
