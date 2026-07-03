---
name: tutorial-book-auditor
description: Audit and improve Spring Boot tutorial books and companion backend example projects for step-by-step teaching quality. Use when reviewing, scoring, or editing chapters, README/docs, validation reports, API contracts, progressive/final examples, controllers, services, repositories, DTOs, Spring Data JPA, PostgreSQL, Flyway, validation, exception handling, auth, JWT, admin APIs, audit logs, testing, Docker, deployment, production-readiness, or 10/10 tutorial content.
---

# Tutorial Book Auditor

## Required Reference

Read `references/teaching-principles.md` before scoring or rewriting chapters. It contains the rubric, chapter checklist, common failure patterns, and verification gates.

When the task touches planning, API behavior, final structure, release quality, or production-readiness, also inspect the relevant files in `docs/internal/`:

- `README.md`
- `book-plan.md`
- `api-contract.md`
- `final-project-structure.md`
- `style-guide.md`
- `release-checklist.md`
- `validation-report.md`

## Workflow

1. Resolve the exact scope: repository, part, chapter, example project, API flow, report, or release gate.
2. Inspect source files with `rg`/file reads. Do not rely on memory when files are available.
3. Compare the chapter against the progressive example at the same learning point and the final example when relevant.
4. Check that routes, DTOs, errors, auth behavior, and admin behavior match `docs/internal/api-contract.md`.
5. Score with the 10-point rubric from the reference file when reviewing teaching quality.
6. Lead with concrete blockers: missing explanation, skipped folder/file setup, hidden class usage, long code blocks, wrong ports/routes, missing expected responses, or unsynced examples.
7. If the user asks to fix, edit the smallest set of files that makes the lesson easier and correct.
8. Verify with commands that match the touched scope.

## Editing Rules

- Explain a new Spring Boot concept before asking the learner to use it.
- Split code into small, teachable blocks instead of replacing whole files with long code.
- Include folder/file creation commands before a lesson creates new files.
- Include Windows PowerShell and macOS/Linux Bash commands when the action differs by OS.
- Keep ports, routes, package names, class names, filenames, DTO names, status codes, and expected responses aligned with the actual example project.
- Update docs, examples, README, validation report, API contract notes, Postman files, and migration notes together when behavior changes.
- Preserve the progressive learning path. Do not introduce future-chapter fields, classes, packages, Flyway migrations, auth behavior, or security hardening without an explanation.
- Make chapters self-contained. A learner should not need to open example source files to understand what to write, where to write it, or how to verify it.
- Use example projects as source of truth for correctness and optional reference only. If an example path is mentioned, the chapter still needs to include the relevant teaching content.

## Spring Boot Checks

- For controller lessons, check annotations, request mappings, request/response DTOs, validation annotations, status codes, and Postman/request examples.
- For architecture lessons, check package structure, service/repository boundaries, constructor injection, mapper code, and naming consistency.
- For database lessons, check Gradle dependencies, PostgreSQL settings, JPA entities, repositories, transactions, Flyway migrations, seed data, and profiles.
- For validation/error lessons, check Bean Validation, custom messages, global exception handling, field errors, error response shape, and expected response bodies.
- For auth/admin lessons, check password hashing, JWT configuration, filters, security config, roles, `401` vs `403`, self-protection, audit logs, pagination/filter/sort, and security notes.
- For production-ready claims, check profiles/env vars, secrets, CORS, transactions, logging, OpenAPI, tests, Docker, build artifacts, and deployment notes.

## Verification

Run only commands relevant to the files touched:

- Book/docs changes: `npm run build`.
- Spring Boot example changes: relevant Gradle tests, usually `.\gradlew.bat test` on Windows or `./gradlew test` on macOS/Linux.
- Runtime/deployment changes: relevant `bootJar` or build command.
- Docker Compose changes: `docker compose config` in the touched compose directory.
- Dependency or security changes: run the relevant Gradle/npm audit or build command and update `docs/internal/validation-report.md`.

If a command cannot be run, report the reason directly.

## Response Shape

For reviews, return score, findings by chapter/file, and the next edits needed. For fixes, return changed files and verification results. Keep the answer concise and grounded in file paths.
