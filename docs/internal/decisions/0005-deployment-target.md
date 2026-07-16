---
title: '0005 - Deployment Target'
description: เหตุผลของ GitHub Pages และ Docker Compose deployment path
---

# 0005 - Deployment Target

## Status

Accepted

## Decision

ใช้ GitHub Pages สำหรับเว็บไซต์หนังสือ และใช้ Docker/Docker Compose เป็น deployment practice สำหรับ backend example

## Context

หนังสือต้องเผยแพร่ได้ง่ายและควรมี production-ready path ให้ผู้เรียนเห็นการ build/run API จริงร่วมกับ PostgreSQL

## Consequences

ข้อดี:

- GitHub Pages เหมาะกับ static book site
- Docker Compose ทำให้ API + PostgreSQL reproducible
- release QA ตรวจได้ด้วยคำสั่งมาตรฐาน

ข้อเสีย:

- ต้องจัดการ base path ของ GitHub Pages
- Docker ต้องพร้อมในเครื่องผู้ตรวจ
- production hosting จริงยังต้องอธิบายเป็น checklist มากกว่าเจาะ provider เดียว
