---
name: full-build
description: Run the full renovation buildAll pipeline (clean → style → test → IT → assemble → docker → e2e → down). Use when the user says "full build", "run buildAll", "ba", or "verify everything before PR".
allowed-tools: Bash
---

# full-build

Run the project's canonical `buildAll` pipeline and surface only failures.

## Procedure

1. Warn the user this will take several minutes and starts/stops Docker Compose. Confirm before running if Docker isn't already up.

2. Run:
   ```bash
   ./gradlew buildAll 2>&1 | tee /tmp/renovation-buildAll.log
   ```
   `buildAll` alias chain: clean → detekt → ktlintCheck → test → integrationTest → assemble → frontend npm build → docker compose up → e2eTest → docker compose down.

3. On success, report test totals and exit. On failure:
   - Extract only failing lines:
     ```bash
     grep -E "FAILED|BUILD FAILED|\\.kt:|> Task .* FAILED" /tmp/renovation-buildAll.log | head -80
     ```
   - Identify which stage failed (style, unit test, IT, e2e, docker).
   - Do NOT attempt auto-fixes mid-build — report first, let the user decide.

4. If Docker Compose is left running after a failure mid-pipeline, offer `/stack-down`.

## Notes

- The Gradle alias is `ba` — both trigger `buildAll`.
- `./gradlew all` is a superset (buildAll + ktlint + kover + dependency check) — use it for release candidates.
- If you only need the light loop: `./gradlew test integrationTest` without docker/e2e.
