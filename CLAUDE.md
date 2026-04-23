# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Renovation** is a microservices demo application showcasing enterprise-grade architecture patterns. It uses Kotlin + Spring Boot for backend services, Vue.js 3 for frontends, and is deployable via Docker Compose or Kubernetes.

## Gradle Modules

Defined in `settings.gradle.kts`:
- `:backend` — REST API (Spring Boot, PostgreSQL, Redis)
- `:info` — GraphQL API (Spring Boot, MongoDB, Netflix DGS)
- `:gateway` — API Gateway (Spring Cloud Function, OAuth2 client)
- `:frontend` — Vue.js 3 SPA, built and served as static files by `:backend`
- `:frontend-info` — Standalone Vue.js 3 SPA for the Info service
- `:common` — Shared Kotlin library, dependency of all other services. Provides `Rest.kt` (Rest Assured `given(port, token)` helpers), `JwtUtils.kt` (Keycloak JWT role extraction), and `GrantTypeAccessToken` implementations (`PasswordGrantTypeAccessToken`, `ClientCredentialsGrantTypeAccessToken`)
- `:api-test` — JUnit 5 API/integration test suite

## Common Commands

### Build & Test
```bash
./gradlew buildAll          # clean → detekt → ktlintCheck → test → integrationTest → assemble → frontend npm build → docker compose up → e2eTest → docker compose down (alias: ba)
./gradlew all               # buildAll + ktlint + kover + dependency check
./gradlew test              # unit tests only
./gradlew integrationTest   # @integrationTest tagged tests (uses Testcontainers)
./gradlew e2eTest           # @e2eTest tagged tests
./gradlew ktlintCheck       # check Kotlin code style
./gradlew ktlintFormat      # auto-fix Kotlin code style
./gradlew detekt            # static analysis
./gradlew installGitHooks   # install pre-push + commit-msg hooks
```

### Docker Compose (local development)
```bash
docker compose up           # start all services (requires Keycloak/security)
docker compose -f docker-compose-no-security.yml up  # start without Keycloak
docker compose -f docker-compose.yml -f docker-compose-debug.yml up  # with JDWP debug ports (backend:5005, info:5006, gateway:5007)
docker compose down
```

### Frontend (npm)
Both `frontend/` and `frontend-info/` support:
```bash
npm run serve   # local dev server
npm run build   # production build
npm run clean   # remove dist + node_modules
```
The Gradle `copyDistToPublic` task copies `frontend/dist/` into `backend/src/main/resources/public/`.

### Kubernetes
```bash
./gradlew k8sApiTest                # run minikubeTest suite against cluster
./gradlew k8sIngressApiTest         # run minikubeTest suite against cluster via Ingress
./gradlew k8sUploadBackendImage     # build + load backend image into minikube
./gradlew k8sUploadInfoImage        # build + load info image into minikube
./gradlew k8sUploadFrontendInfoImage
```

### Makefile shortcuts (wraps Gradle + Docker)
```bash
make build        # full build pipeline
make docker_all   # build + docker compose up
make docker_down  # stop docker
make style        # ktlint check
make check_all    # all checks
```

## Architecture

### Request Flow
Browser → Gateway (port 8085) → Backend (8080) or Info (9090)

- **Gateway** handles OAuth2 login (Keycloak), social login (GitHub, Google), and routing
- **Backend** exposes REST API, serves the Vue.js frontend as static resources, uses PostgreSQL + Redis
- **Info** exposes a GraphQL API, uses MongoDB
- **Frontend** is bundled into Backend's JAR via the Gradle build
- **Frontend-Info** is a standalone SPA that talks directly to the Info GraphQL endpoint

### Security
- Keycloak (port 18080 locally) is the OIDC identity provider for `renovation-realm`
- Gateway acts as OAuth2 client; Backend and Info are OAuth2 resource servers
- In Kubernetes, HashiCorp Vault + External Secrets Operator manages secrets
- `docker-compose-no-security.yml` bypasses Keycloak for easier local development

### Data Layer
| Store | Service | Purpose |
|-------|---------|---------|
| PostgreSQL | Backend | Relational data, Liquibase migrations |
| MongoDB | Info | Document storage, init scripts in `info/src/main/resources/db/migration/mongo/init/` |
| Redis | Backend | Caching and session management |

### Infrastructure Services
Prometheus + Grafana (metrics), Jaeger (distributed tracing), Loki (log aggregation). All configured in Docker Compose and Kubernetes Helm charts.

## Kubernetes / Helm
- Helm charts live in `a-deploy/charts/`
- Deployment scripts in `auxiliary/deployment/minikube/` (single/HA cluster setup)
- Namespace structure: `security`, `db`, `apps`, `istio-system`
- `a-deploy/util/cluster/releasing_in_cluster.sh` — deploy all services to cluster

## Test Strategy
Tests use JUnit 5 tags to separate test types:
- `@integrationTest` — requires Testcontainers (Docker)
- `@e2eTest` — full stack via Docker Compose
- `@minikubeTest` — runs against a live Kubernetes cluster

Run a single test class: `./gradlew :backend:test --tests "renovation.backend.SomeTest"`

Testcontainers images used in integration tests:
- Keycloak: `quay.io/keycloak/keycloak:18.0.2`
- PostgreSQL: `postgres:16.1-alpine`
- Redis: `redis:7.2.3-alpine`

## Code Quality
- **ktlint 1.0.1** enforces Kotlin style (CI-required)
- **detekt 1.23.4** for static analysis
- **Kover** for code coverage (100% target with configured exclusions)
- **OWASP DependencyCheck** for vulnerability scanning
- Git hooks enforce a custom commit format (`#<task-number> <description>` or `WIP <description>`, max 80 chars for the description) and run checks pre-push

## CI/CD
GitHub Actions (`.github/workflows/build.yml`) runs `./gradlew buildAll` on pushes to `develop`/`ai` and PRs to `develop`. Slack notification on failure via `ACTION_MONITORING_SLACK` secret.

Additional workflows: `helm-release.yaml` (releases Helm charts on push to `develop`), `reusable_project_assemble.yaml` (reusable build workflow).

## Environment
**Prerequisites:** JDK 21 (project targets Java 21; tests use `--enable-preview`)

Local dev uses `.env` file for Docker Compose variable substitution. Key ports:
- Backend: 8280 (host) → 8080 (container)
- Info: 9190 → 9090
- Gateway: 8285 → 8085
- Keycloak: 18080 → 8080
- PostgreSQL: 5532 → 5432
- MongoDB: 27117 → 27017
- Redis: 6479 → 6379

**MCP tools available:** `mcp__postgres-renovation__query`

## Documentation First

Consult official docs via MCP before writing ANY code. Zero tolerance for deprecated code.

- Each skill lists its MCP servers and documentation sources — **load the skill first**
- When in doubt, **query the MCP server first**
- Fallback: `Context7` MCP for any library not covered by a dedicated MCP server

**No Deprecated or Outdated Code:**
- **ALWAYS** use latest stable syntax and features from official documentation
- **NEVER** generate deprecated methods, classes, or patterns
- **ALWAYS** verify API signatures against current documentation before generating code
- **ALWAYS** check for breaking changes in recent versions


### Useful Endpoints (local)
- Swagger UI: `http://localhost:8280/swagger`
- OpenAPI spec: `http://localhost:8280/openapi` (YAML: `/openapi.yaml`)
- Actuator: `http://localhost:8280/actuator/health`, `/actuator/info`, `/actuator/metrics`


## Important Rules
- **Always write tests** for new features

## Claude Code Workflow

- Use **Context7 MCP** proactively for library/API docs — don't wait to be asked
- Commits: semantic message (max 80 chars in header and max 240 chars in context), no `Co-Authored-By` trailer
- Also search for environment variables definition in .claude/setting.json in "env" field

## Compact instructions

When you are using compact, please focus on test output and code changes

## Meta

The human monitors you in an IDE. Minimize mistakes they need to catch. You have unlimited stamina — the human does not. Loop on hard problems, not wrong problems.
