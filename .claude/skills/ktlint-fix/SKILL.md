---
name: ktlint-fix
description: Run ktlintFormat then ktlintCheck + detekt, reporting only the remaining violations. Use when the user says "fix lint", "format kotlin", "run ktlint", or after Kotlin edits.
allowed-tools: Bash
---

# ktlint-fix

Auto-format Kotlin, then verify style + static analysis.

## Procedure

1. Auto-fix what can be fixed:
   ```bash
   ./gradlew ktlintFormat
   ```

2. Re-check — anything remaining is a real violation that needs manual attention:
   ```bash
   ./gradlew ktlintCheck --continue 2>&1 | grep -E "\.kt:|FAILED|BUILD" | head -60
   ```

3. Run detekt (different checks — complexity, naming, etc.):
   ```bash
   ./gradlew detekt --continue 2>&1 | tail -40
   ```

4. If either step shows violations, summarize by file with line numbers. Propose the fix only if the user asks — don't mass-edit unrelated files.

## Notes

- ktlint 1.0.1 is pinned; do not suggest upgrading as part of this skill.
- detekt config is at `auxiliary/code/detekt/config.yml` — rule changes belong there, not in source.
- The pre-push git hook runs ktlintCheck; running this skill before push avoids round-trips.
