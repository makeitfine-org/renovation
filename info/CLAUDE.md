# :info

GraphQL API on Netflix DGS **8.2.0** / Spring Boot 3.2.0 (Kotlin 1.9.21, JDK 21). MongoDB via Spring Data. OAuth2 resource server.

## Package layout

- `renovation.info.data.entity` — Mongo documents
- `renovation.info.data.repository` — Spring Data MongoRepositories
- `renovation.info.data.service.impl` — service layer
- `renovation.info.web.datafetcher` — `@DgsComponent` data fetchers
- `renovation.info.web.controller` — any supplemental REST endpoints (rare)
- `renovation.info.config` — DGS / Spring / security config
- `renovation.info.generated.dgs` — **generated** — do not edit

## GraphQL schema

- Schema file: `src/main/resources/schema/schema.graphqls`
- Code-gen task: `./gradlew :info:generateJava` → output under `build/generated/sources/dgs-codegen/...`
- Use `/dgs-resolver` skill to add a new query/mutation/field.

## MongoDB

- Init scripts: `src/main/resources/db/migration/mongo/init/`
- Read-only MCP access: `mcp__mongodb-renovation__*`
- Local port: 27117 → 27017 (db `infodb`, user `infouser`)

## Tests

```bash
./gradlew :info:test
./gradlew :info:integrationTest            # Testcontainers Mongo
./gradlew :info:integrationTest --tests "FooTest"
```

## Resolver conventions

- `@DgsQuery` for root queries, `@DgsMutation` for mutations.
- `@DgsData(parentType=..., field=...)` for nested resolution. If resolving per-parent, use a `DataLoader` / `@DgsDataLoader` to avoid N+1.
- Keep data fetchers thin — delegate to `@Service` beans.
- Consult DGS 8.x docs via Context7 before guessing method signatures (`rules/no-deprecated.md`).

## Do not

- Edit files under `build/generated/` — they are overwritten on every build.
- Add federation directives (`@key`, `@external`) — this service is not federated.
