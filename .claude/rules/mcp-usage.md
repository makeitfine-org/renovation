---
name: mcp-usage
description: When to reach for each MCP server configured for the renovation project.
---

# MCP Usage

## `postgres-renovation`
Read access to the backend's PostgreSQL.

Use when:
- Inspecting table shape / indexes before writing a Liquibase changeset.
- Verifying a migration ran (`liquibase.databasechangelog`).
- Sanity-checking data state while debugging an integration test.

Env: `RENOVATION_BACKEND_URL` (defined in `.claude/settings.local.json`, not committed).

## `mongodb-renovation`
Read-only MongoDB access for the Info service. Tools:
`list-databases`, `list-collections`, `find`, `aggregate`, `count`, `collection-schema`, `collection-indexes`, `explain`, `search-knowledge`.

Use when:
- Understanding the document shape in `infodb` collections before adding a GraphQL resolver.
- Verifying a mongo init script ran (`info/src/main/resources/db/migration/mongo/init/`).

Env: `MDB_MCP_CONNECTION_STRING` ← `MONGO_DB_URI` (from `.claude/settings.local.json`).

## `context7` (global)
Documentation lookup for **any** library. Use proactively — before writing code that touches a library API, especially Spring Boot 3.2, DGS 8.2.0, Vue 3, Vuex 4.

Flow:
1. `mcp__context7__resolve-library-id` with the library name.
2. `mcp__context7__query-docs` with the resolved ID and your question.

## When NOT to use MCP

- Refactoring project-internal code.
- Running Gradle/npm commands (use Bash).
- Editing files (use Edit/Write).
- Reading project files (use Read/Grep/Glob).
