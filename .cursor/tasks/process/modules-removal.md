# Module Cleanup Plan

## Overview

Remove 6 isolated modules not in core stack, cleaning all references.

## Modules to Remove (in order)

1. ~~**reacty-service**~~ - ✅ COMPLETED
2. ~~**influx-service**~~ - ✅ COMPLETED
3. **batch-service** - Isolated batch jobs
4. ~~**node-server**~~ - ✅ COMPLETED
5. ~~**ng-part**~~ - ✅ COMPLETED
6. **temp** - Vault testing module

## Progress

### ✅ reacty-service (Completed)
- Removed from `settings.gradle.kts` (variable + include)
- Removed from `gradle.properties`
- Removed from `build.gradle.kts` (build task + docker image)
- Deleted module directory
- Verified with `./gradlew projects` and grep

### ✅ influx-service (Completed)
- Removed from `settings.gradle.kts` (variable + include)
- Removed from `gradle.properties`
- Removed from `build.gradle.kts` (build task + docker image)
- Removed from all docker-compose files (service + influxdb dependency)
- Deleted module directory
- Verified with `./gradlew projects` and grep

### ✅ node-server (Completed)
- Removed from `settings.gradle.kts` (variable + include)
- Removed from `gradle.properties`
- Removed from `build.gradle.kts` (npm tasks)
- Deleted module directory
- Verified with `./gradlew projects` and grep

### ✅ ng-part (Completed)
- Removed from `settings.gradle.kts` (variable + include)
- Removed from `gradle.properties`
- Removed from `build.gradle.kts` (npm tasks)
- Removed from `.github/workflows/reusable_project_assemble.yaml` (npm task)
- Removed CORS config for Angular ports (4200, 98*)
- Deleted module directory
- Verified with `./gradlew projects` and grep

## Per-Module Removal Steps

### For Each Module:

**A. Gradle Configuration**

- Remove from `settings.gradle.kts`:
- Module name variable declaration (line ~120-127)
- `include()` statement (line ~129-143)
- Remove from `gradle.properties`:
- Module name definition (line 30-37)
- Remove from `build.gradle.kts`:
- Build task references (lines 279-315)
- Docker image removal (lines 416-425)
- Module array if present (line 36 for temp)

**B. Docker Compose**

- Remove service definition from `docker-compose.yml`
- Remove dependent services if isolated (e.g., influxdb for influx-service)

**C. Module Directory**

- Delete entire module directory: `/home/eug/dev/projects/my/renovation/{module-name}/`

**D. Verify**

- Run `./gradlew projects` to confirm removal
- Check no broken references with grep

## Execution Order

Each module approved individually before proceeding to next.
