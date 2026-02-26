# Renovation Project: Improvements & Optimizations Plan

## Context
This is a holistic improvement proposal for the **Renovation** microservices demo app. The project already has solid architectural foundations (OAuth2/OIDC, read/write DB splitting, soft deletes, Testcontainers-based tests, Helm/K8s support) but has accumulated tech debt, missing production-readiness features, and several code quality issues across all modules.

---

## Critical Issues (P0 — Fix First)

### 1. Backend: Missing Pagination
**Problem:** `WorkRepository.findAll()` and `findByTitleLike()` load the full table without limits.
**Fix:** Add `Pageable` parameter to `WorkRepository` and propagate through `WorkService`/`WorkController`.
- Files: `backend/src/main/kotlin/renovation/backend/data/repository/WorkRepository.kt`
- Files: `backend/src/main/kotlin/renovation/backend/data/service/impl/WorkServiceImpl.kt`
- Files: `backend/src/main/kotlin/renovation/backend/web/controller/WorkController.kt`

### 2. Info: In-Memory GraphQL Filtering (N+1)
**Problem:** `DetailsDataFetcher` fetches all MongoDB documents then filters in-memory via `.stream().filter()`.
**Fix:** Push filtering down to `DetailsRepository` using `@Query` or Spring Data method expressions.
- Files: `info/src/main/kotlin/renovation/info/web/datafetcher/DetailsDataFetcher.kt`
- Files: `info/src/main/kotlin/renovation/info/data/repository/DetailsRepository.kt`

### 3. Backend: Overly Permissive CORS
**Problem:** `@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])` — the pattern `"http://r"` matches any domain starting with `r`.
**Fix:** Replace with specific origins or extract CORS to `SecurityConfig` using `CorsConfigurationSource`.
- Files: `backend/src/main/kotlin/renovation/backend/web/controller/WorkController.kt:40`
- Files: `backend/src/main/kotlin/renovation/backend/web/controller/WorkerController.kt:17`
- Files: `backend/src/main/kotlin/renovation/backend/web/controller/ServiceController.kt:19`
- Files: `info/src/main/kotlin/renovation/info/config/CorsConfig.kt` (hardcoded `192.168.0.113`)

### 4. Secrets in Version Control
**Problem:** `.env` file contains plaintext passwords; `application.yml` has hardcoded Keycloak client secret fallback.
**Fix:** Replace `.env` with `.env.example`; remove secret fallbacks from YAMLs.
- Files: `.env` → rename to `.env.example` with placeholder values
- Files: `backend/src/main/resources/application.yml:118`
- Files: `gateway/src/main/resources/application.yml`

---

## High Priority (P1 — Important)

### 5. Backend: Cache Name Mismatch
**Problem:** `ServiceController` evicts `"works"` but `WorkServiceCacheableImpl` defines cache as `CACHE_WORKS_ALL`.
**Fix:** Use the constant from `WorkServiceCacheableImpl` in `ServiceController`.
- Files: `backend/src/main/kotlin/renovation/backend/web/controller/ServiceController.kt:26`
- Files: `backend/src/main/kotlin/renovation/backend/data/service/impl/WorkServiceCacheableImpl.kt:30`

### 6. Backend: Incomplete Cache Implementation (TODO)
**Problem:** Only `findAll()` and `findById()` are cached; `findByTitleLike()` is not.
**Fix:** Add `@Cacheable` to `findByTitleLike` in `WorkServiceCacheableImpl`.
- Files: `backend/src/main/kotlin/renovation/backend/data/service/impl/WorkServiceCacheableImpl.kt:21`

### 7. Info: Custom Exception Types
**Problem:** `TodoServiceImpl` throws `RuntimeException()` directly (suppresses detekt warning).
**Fix:** Create `TodoNotFoundException` or similar; remove `@Suppress("TooGenericExceptionThrown")`.
- Files: `info/src/main/kotlin/renovation/info/data/service/impl/TodoServiceImpl.kt:20`

### 8. Backend: GraphQL HTTP Client (String Hacking)
**Problem:** `GraphQlServiceImpl` manually escapes newlines and builds raw JSON strings for GraphQL requests.
**Fix:** Use Spring's `GraphQlClient` or structured JSON serialization.
- Files: `backend/src/main/kotlin/renovation/backend/data/service/impl/GraphQlServiceImpl.kt:59`

### 9. Frontend: Remove `console.log` Statements
**Problem:** 20+ `console.log()` calls in production frontend code.
**Fix:** Remove all `console.log` from `store/modules/work.js`, `worker.js`, and services.
- Files: `frontend/src/store/modules/work.js`
- Files: `frontend/src/store/modules/worker.js`

### 10. Frontend-Info: Fix Environment Variable Configuration (TODO)
**Problem:** `http-common.js` has commented-out env var setup with a TODO; GraphQL endpoint is hardcoded to `localhost`.
**Fix:** Implement `VUE_APP_*` env var loading for base URLs.
- Files: `frontend-info/src/http-common.js`
- Files: `frontend-info/src/service/WorkerDataService.js`

### 11. Frontend: Invalid CSS Value
**Problem:** `color: official;` in `Loading.vue:20` is not a valid CSS value.
**Fix:** Replace with a valid color (e.g., `color: #0d6efd;`).
- Files: `frontend/src/component/Loading.vue:20`

### 12. Backend: Missing DB Index for LIKE Queries
**Problem:** `findByTitleLike()` runs without an index on `work.title`.
**Fix:** Add a Liquibase changeset creating an index on the `title` column.
- Files: `backend/src/main/resources/db/liquibase/changelog.xml`

### 13. Frontend: Upgrade Axios & Node Versions
**Problem:** `frontend-info` uses Axios 0.24.0 vs 1.7.3 in `frontend`; Node 16 (EOL) in Dockerfiles.
**Fix:** Align both frontends to Axios 1.7.x; upgrade `frontend-info/Dockerfile` to `node:20-alpine`.
- Files: `frontend-info/package.json`
- Files: `frontend-info/Dockerfile`

---

## Medium Priority (P2 — Next Iteration)

### 14. CI/CD: Add Dependency Caching
**Problem:** GitHub Actions workflow has no Gradle or npm caching → slow builds.
**Fix:** Add `actions/cache` steps for `~/.gradle/caches` and `frontend/node_modules`.
- Files: `.github/workflows/build.yml`

### 15. CI/CD: Separate Test Tiers as Parallel Jobs
**Problem:** Single monolithic `buildAll` job runs everything serially within 15-min timeout.
**Fix:** Split into `unit-test`, `integration-test`, `e2e-test` jobs with `needs:` dependencies; extend timeout.
- Files: `.github/workflows/build.yml`

### 16. CI/CD: Add Code Coverage Report Publishing
**Problem:** Kover runs but coverage report is never published to GitHub Actions summary.
**Fix:** Add `koverXmlReport` step and upload with `actions/upload-artifact` or PR comment.
- Files: `.github/workflows/build.yml`

### 17. Docker: Multi-Stage Builds + JRE Base Image
**Problem:** Dockerfiles use full JDK; no layer optimization; fixed `SNAPSHOT` JAR name.
**Fix:** Add a builder stage; use `eclipse-temurin:21-jre-alpine` for runtime; parametrize JAR name.
- Files: `backend/Dockerfile`, `info/Dockerfile`, `gateway/Dockerfile`

### 18. Kubernetes: Add Resource Limits & Health Probes
**Problem:** Backend and Info deployments have no CPU/memory limits, no liveness/readiness probes.
**Fix:** Add `resources.requests/limits`; add `/actuator/health` liveness and readiness probes.
- Files: `auxiliary/deployment/k8s/yaml/backend/backend-deployment.yaml`
- Files: `auxiliary/deployment/k8s/yaml/info/info-deployment.yaml`
- Files: `auxiliary/deployment/k8s/yaml/frontend-info/frontend-info-deployment.yaml`

### 19. Kubernetes: Enable TLS / HTTPS Ingress
**Problem:** Ingress has no TLS configuration; no cert-manager integration.
**Fix:** Add cert-manager `ClusterIssuer` + `tls:` block in ingress manifest.
- Files: `auxiliary/deployment/k8s/yaml/renovation-ingress.yaml`

### 20. Kubernetes: Upgrade PostgreSQL to Supported Version
**Problem:** PostgreSQL HA uses version 11.12 (EOL).
**Fix:** Update Bitnami chart to PostgreSQL 16.x; update `postgresql-chart/Chart.yaml`.
- Files: `a-deploy/charts/postgresql-chart/Chart.yaml`

### 21. Logging: Standardize to KotlinLogging
**Problem:** Mix of `LoggerFactory.getLogger()` and `KotlinLogging.logger {}` across backend.
**Fix:** Replace all SLF4J direct usages with `KotlinLogging.logger {}`.
- Files: Multiple backend config and service files

### 22. Info: Extract DetailsModel Service Layer (TODO)
**Problem:** `DetailsService.kt` has TODO: "create DetailsModel and use it in service".
**Fix:** Create `DetailsModel` data class; map entity → model in service layer.
- Files: `info/src/main/kotlin/renovation/info/data/service/DetailsService.kt`
- Files: `info/src/main/kotlin/renovation/info/data/service/impl/DetailsServiceImpl.kt`

---

## Low Priority (P3 — Nice to Have)

### 23. Backend: Consolidate CORS into SecurityConfig
Instead of `@CrossOrigin` on each controller, configure `CorsConfigurationSource` once in `SecurityConfig`.

### 24. Frontend: Add Component Tests (Vitest/Jest)
Zero frontend test coverage. Add Vitest for Vue components: at minimum `WorkList`, `AddWork`, `Work`.

### 25. GraphQL: Add Query Complexity Limits
No depth/complexity limits on GraphQL queries expose the Info service to expensive introspection/nested queries.
Add `MaxQueryComplexityInstrumentation` via Spring DGS configuration.

### 26. Observability: Structured JSON Logging + OpenTelemetry
Add `logstash-logback-encoder` for JSON logs; integrate OpenTelemetry auto-instrumentation for distributed tracing (Jaeger is already referenced).

### 27. CI/CD: Add Container Image Vulnerability Scanning
Add a `trivy` scan step after Docker build in GitHub Actions.

### 28. Kubernetes: Add Network Policies
Define `NetworkPolicy` manifests to restrict pod-to-pod traffic to only necessary paths.

### 29. Backend: RouteController Simplification (TODO)
Comment in `RouteController.kt:18` asks to simplify or move to config. Consolidate forward-routing.

---

## Verification Plan

For each change above, validate as follows:

| Area | How to Verify |
|------|--------------|
| Pagination | `./gradlew :backend:test --tests "*WorkControllerTest*"` + API call with `?page=0&size=5` |
| GraphQL filter | `./gradlew :info:integrationTest` + query with filter param |
| CORS | Browser DevTools → Network tab; check `Access-Control-Allow-Origin` |
| Cache mismatch | `./gradlew :backend:test --tests "*ServiceControllerTest*"` + `GET /api/service/cache/evict` |
| CI/CD caching | Check GitHub Actions log for "Cache hit" lines |
| Docker build | `docker build -t test . && docker image ls test` → verify smaller size |
| K8s probes | `kubectl describe pod <backend-pod>` → "Liveness: ..." present |
| Frontend tests | `cd frontend && npm run test:unit` |
| Full stack | `./gradlew buildAll` → all e2e tests green |
