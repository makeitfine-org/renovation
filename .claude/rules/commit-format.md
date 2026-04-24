---
name: commit-format
description: Renovation commit message format enforced by git hooks.
---

# Commit Format

The repo installs git hooks via `./gradlew installGitHooks` (source: `auxiliary/code/githooks/`). The `commit-msg` hook rejects messages that do not match one of:

```
#<task-number> <description>
WIP <description>
```

**Rules**
- `<task-number>` is 1–4 digits, matching the GitHub issue number.
- `<description>` is 1–80 characters. The header (entire message first line) stays under 80 chars too.
- Body is optional; if present, wrap at 240 characters per paragraph.
- **Do not** add `Co-Authored-By:` trailers (root `CLAUDE.md` line 156).

**Examples**

Good:
```
#124 Add liquibase changeset for work.priority column
WIP refactor cache key naming
```

Bad (why):
- `Update backend` — no task number
- `#124: add column` — colon after number
- `#12345 add column` — task number exceeds 4 digits
- Description >80 chars — hook rejects

**Advisory Claude hook**
`.claude/hooks/validate-commit-msg.sh` warns on mismatch but never blocks — the git `commit-msg` hook is the authoritative gate.
