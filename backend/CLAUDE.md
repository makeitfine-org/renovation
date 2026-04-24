# :backend

REST API on Spring Boot 3.2.0 (Kotlin 1.9.21, JDK 21). PostgreSQL via Spring Data JPA + Liquibase. Redis for caching. OAuth2 resource server. Serves the bundled `:frontend` SPA from `src/main/resources/public/`.

## Package layout

- `renovation.backend.data.entity` — JPA entities
- `renovation.backend.data.repository` — Spring Data JPA repositories
- `renovation.backend.data.service.impl` — service layer (transactional)
- `renovation.backend.web.controller` — REST controllers (`@RestController`)
- `renovation.backend.web.interceptor` — request interceptors
- `renovation.backend.config` — Spring config, security, cache, OpenAPI

## Liquibase

- Master changelog: `src/main/resources/db/liquibase/changelog.xml`
- Changesets under: `src/main/resources/db/liquibase/changesets/`
- Use the `/new-liquibase-changeset` skill to scaffold new ones. Never edit an applied changeset — add a new one.

## Tests

| Command | Purpose |
|---|---|
| `./gradlew :backend:test` | unit tests |
| `./gradlew :backend:integrationTest` | `@integrationTest` — Testcontainers PostgreSQL 16.1-alpine + Redis 7.2.3-alpine + Keycloak 18.0.2 |
| `./gradlew :backend:test --tests "renovation.backend.Foo"` | single class |

Integration tests must use a real DB — never mock (`rules/testing.md`).

## Build + frontend bundling

- `./gradlew :backend:bootRun` — dev server on `http://localhost:8080`
- `./gradlew :backend:build` — builds JAR (executes `:frontend:npmBuild` + `copyDistToPublic` as part of the dependency graph)
- Frontend assets land at `src/main/resources/public/` — **do not edit them directly** (they are regenerated).

## Observability

- Actuator: `/actuator/health`, `/actuator/info`, `/actuator/metrics`, `/actuator/prometheus`
- Swagger UI: `/swagger` (OpenAPI JSON at `/openapi`, YAML at `/openapi.yaml`)

## Cache

Redis-backed Spring Cache. Cache name constants live under `renovation.backend.config` — keep string keys in one place to avoid name drift (there is a known tech-debt item for cache-name mismatches in `.claude/plans/linear-nibbling-valiant.md`).
