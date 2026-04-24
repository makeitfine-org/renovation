# :common

Shared Kotlin library. Published to local Maven (`~/.m2`) via `./gradlew :common:publishToMavenLocal`, which runs as part of the normal build for dependent modules (`:backend`, `:info`, `:gateway`, `:api-test`).

## What lives here

- `renovation.common.web.Rest` — Rest Assured helpers, notably `given(port, token)` for authenticated request building.
- `renovation.common.security.jwt.JwtUtils` — Keycloak JWT parsing and role extraction.
- `renovation.common.security.iam.impl.PasswordGrantTypeAccessToken` — token fetch via password grant (test-only).
- `renovation.common.security.iam.impl.ClientCredentialsGrantTypeAccessToken` — token fetch via client-credentials grant.
- General utilities under `renovation.common.util`.

## Rules

- This module is consumed by **every** other module — keep it small, cohesive, and stable.
- No Spring Boot starter dependencies here (it would pollute consumers). Use `spring-context` or plain Kotlin where possible.
- Breaking changes require a coordinated PR across all dependents.

## Tests

```bash
./gradlew :common:test
```

Unit tests only — this module has no Testcontainer needs.

## Publishing

Dependent modules pull `renovation:common:<version>` from local Maven. If you change `:common`, run:
```bash
./gradlew :common:publishToMavenLocal
```
before rebuilding consumers. The buildAll pipeline handles this automatically.
