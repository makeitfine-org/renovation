---
name: new-liquibase-changeset
description: Scaffold a new Liquibase changeset under backend and register it in the master changelog. Use when the user says "add migration", "new liquibase changeset", "alter the schema", or "add a column/table/index".
allowed-tools: Bash, Read, Write, Edit, Glob, mcp__postgres-renovation__query
---

# new-liquibase-changeset

Create a new Liquibase changeset under `:backend` and wire it into the master changelog.

## Files of interest

- **Master changelog:** `backend/src/main/resources/db/liquibase/changelog.xml` (include list)
- **Changesets root:** `backend/src/main/resources/db/liquibase/changesets/` (follow existing file naming)
- **Current schema:** query live via `mcp__postgres-renovation__query` before changing anything

## Procedure

1. Inspect the current schema so the migration targets real tables/columns:
   ```
   mcp__postgres-renovation__query — e.g. "\d renovation.work" or a relevant SELECT
   ```

2. Pick a name. Follow existing convention in `db/liquibase/changesets/` (look at 1–3 recent files to match the pattern — typically `NN-<verb>-<object>.xml` or dated).

3. Author the changeset:
   - One `<changeSet id="..." author="...">` per file (unique id, usually `<issue-num>-<slug>`).
   - Prefer Liquibase-idiomatic tags (`<addColumn>`, `<createIndex>`, `<addForeignKeyConstraint>`) over `<sql>`.
   - **Always include a `<rollback>`** unless the op is auto-rollbackable.
   - Precondition with `<preConditions onFail="MARK_RAN">` if the changeset is conditionally safe to re-run.

4. Register it in `changelog.xml` as an `<include file="changesets/..."/>` in order — don't reorder existing entries.

5. Test the migration locally:
   ```bash
   /stack-up    # if not already running
   ./gradlew :backend:bootRun   # or restart the backend container
   # Verify via MCP query:
   #   SELECT * FROM renovation.databasechangelog ORDER BY dateexecuted DESC LIMIT 5;
   ```

6. Write an `@integrationTest` that exercises the new column/table if the migration supports a code path being added in the same PR.

## Notes

- PostgreSQL 16.1-alpine (Testcontainers) — stay within its SQL dialect.
- Do NOT use `<dropColumn>` without coordinating — it's destructive.
- Never edit an already-applied changeset; add a new one that fixes it.
