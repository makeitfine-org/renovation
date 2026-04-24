---
name: testing
description: Test tagging, Testcontainers, and integration-test conventions.
---

# Testing Rules

## JUnit 5 tags — strict separation

| Tag | Command | Requires |
|---|---|---|
| (untagged) | `./gradlew test` | nothing — runs in every CI build |
| `@integrationTest` | `./gradlew integrationTest` | Docker (Testcontainers) |
| `@e2eTest` | `./gradlew e2eTest` | Full Docker Compose stack running |
| `@minikubeTest` | `./gradlew k8sApiTest` / `k8sIngressApiTest` | Live minikube cluster with services deployed |

**Rule:** A test that needs a DB/Keycloak/Redis must be `@integrationTest` or `@e2eTest` — never leave it as a plain unit test, or it will fail flakily in CI.

## Testcontainers — pinned images

Do **not** bump these without a project-wide decision:

| Image | Version | Used by |
|---|---|---|
| Keycloak | `quay.io/keycloak/keycloak:18.0.2` | :backend, :gateway |
| PostgreSQL | `postgres:16.1-alpine` | :backend |
| Redis | `redis:7.2.3-alpine` | :backend |
| MongoDB | managed (see `:info`) | :info |

## Do NOT mock the DB in @integrationTest

Use a real Testcontainer. Mocks mask migration/driver regressions that only surface in prod.

## :gateway parallel forks

Gateway integration tests are sensitive to parallel execution — `maxParallelForks` is commented out in root `build.gradle.kts` (line 143). If you add a new IT test there, assume sequential execution and don't rely on test-ordering independence for Keycloak state.

## Single-class runs
```bash
./gradlew :backend:test --tests "renovation.backend.data.service.impl.WorkServiceCacheTest"
./gradlew :gateway:integrationTest --tests "SocialLoginTest"
```

## Coverage

Kover target is 100% on new code. Exclusions for generated DGS code and Spring main classes are pre-configured.
