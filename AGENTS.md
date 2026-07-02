# Agent Instructions

## Scope

These instructions apply to this repository.

## Book Workflow

- Use `skills/tutorial-book-auditor` for any request that reviews or edits tutorial content, example projects, README files, validation reports, or teaching quality.
- Read `skills/tutorial-book-auditor/SKILL.md` first. Load `skills/tutorial-book-auditor/references/teaching-principles.md` before scoring or rewriting chapters.
- Keep lessons step by step: explain new Spring Boot concepts, annotations, methods, packages, commands, Gradle configuration, and application settings before using them.
- Keep progressive examples aligned with the chapter state and final examples aligned with production-grade reference behavior.
- Avoid long pasted code blocks in chapters when a smaller sequence with explanation is possible.
- When creating folders or files in lessons, include copyable Windows PowerShell and macOS/Linux Bash commands when useful.

## Verification

- Run `npm run build` after docs, navigation, or frontmatter changes.
- Run relevant Gradle tests when Spring Boot example code changes.
- Run Docker/Compose validation in touched compose directories when Docker Compose changes.
- Report commands that were run and any command that could not be run.

## Git

- Do not commit or push unless explicitly asked.
- Do not revert user changes unless explicitly asked.
