---
name: dgs-resolver
description: Add or modify a Netflix DGS GraphQL resolver in :info — update schema, add the @DgsComponent resolver, regenerate types, and verify. Use when the user says "add graphql resolver", "new DGS query", "add a field to schema".
allowed-tools: Bash, Read, Write, Edit, Glob, Grep, mcp__mongodb-renovation__collection-schema, mcp__mongodb-renovation__find, mcp__context7__resolve-library-id, mcp__context7__query-docs
---

# dgs-resolver

Add or extend a resolver in the Info service (Netflix DGS 8.2.0 on Spring Boot 3.2.0).

## Files of interest

- **Schema:** `info/src/main/resources/schema/schema.graphqls`
- **Resolvers:** `info/src/main/kotlin/.../web/datafetcher/` (`@DgsComponent`)
- **Codegen output package:** `renovation.info.generated.dgs` (do not edit these files — regenerate instead)
- **Mongo entities:** `info/src/main/kotlin/.../data/entity/`
- **Mongo repos:**    `info/src/main/kotlin/.../data/repository/`

## Procedure

1. **Look up the latest DGS API** (project uses 8.2.0):
   ```
   mcp__context7__resolve-library-id  →  mcp__context7__query-docs
     library: netflix/dgs-framework
     question: e.g. "DataLoader for @DgsData in 8.x", "preferred @DgsQuery signature"
   ```

2. **Edit the schema** at `info/src/main/resources/schema/schema.graphqls`. Keep ordering stable — put new types next to related ones.

3. **Inspect the underlying Mongo collection** if it's a new data source:
   ```
   mcp__mongodb-renovation__list-collections
   mcp__mongodb-renovation__collection-schema <db>.<collection>
   ```

4. **Write the resolver** as `@DgsComponent`:
   - `@DgsQuery` for root queries, `@DgsMutation` for mutations.
   - `@DgsData(parentType = "...", field = "...")` for nested field resolution — use a `DataLoader` if it would otherwise be an N+1.
   - Keep data-fetcher classes thin; delegate to a `@Service`.

5. **Regenerate types**:
   ```bash
   ./gradlew :info:generateJava
   ```
   The generated code ends up under `info/build/generated/sources/dgs-codegen/renovation/info/generated/dgs/`.

6. **Verify** with an integration test:
   ```bash
   ./gradlew :info:integrationTest --tests "<YourDgsTest>"
   ```

## Notes

- N+1: If the new field resolves per-parent and the source is a repository call, use a `BatchLoader` / `DataLoader` — see existing `@DgsDataLoader` examples in the codebase before writing a new one.
- Federation: `:info` does not currently participate in Apollo Federation; don't add `@key` directives unless that changes.
- Do NOT edit files under `info/build/generated/` — they are overwritten on every build.
