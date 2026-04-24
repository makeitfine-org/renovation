---
name: kotlin-style
description: Kotlin code style rules and tooling versions for the renovation project.
---

# Kotlin Style

**Versions (pinned)**
- Kotlin 1.9.21
- JDK 21 (tests use `--enable-preview`)
- Spring Boot 3.2.0
- ktlint 1.0.1 — configuration is implicit defaults
- detekt 1.23.4 — config at `auxiliary/code/detekt/config.yml`
- Kover — 100% line coverage target (exclusions configured in root `build.gradle.kts`)

**Before committing**
```bash
./gradlew ktlintFormat   # auto-fix
./gradlew ktlintCheck    # verify
./gradlew detekt         # static analysis
```

Pre-push git hook runs `check` targets and will reject the push on failure.

**Conventions**
- Do NOT use wildcard imports; ktlint flags them.
- Prefer `val` over `var`; immutable collections unless mutation is needed.
- Constructor injection for Spring beans — no `@Autowired` on fields.
- Use `@ConfigurationProperties` for typed config, not `@Value`.
- Exception handling: do **not** swallow errors or log-and-return-null. Let Spring's handlers convert them or throw a typed domain exception.
- For coroutines / reactive code: prefer structured concurrency (`coroutineScope`, `supervisorScope`).

**No deprecated APIs** — see `no-deprecated.md`.
