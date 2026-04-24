---
name: run-api-test
description: Bring up the Docker Compose stack, run :api-test against it, and tear down. Use when the user says "run api tests", "run api-test", or "verify REST contract".
allowed-tools: Bash
---

# run-api-test

Run the `:api-test` module against a live stack.

## Procedure

1. Start the stack if it is not already healthy:
   ```bash
   if ! curl -sf http://localhost:8280/actuator/health >/dev/null; then
     docker compose -f docker-compose-no-security.yml up -d
     for i in $(seq 1 30); do
       curl -sf http://localhost:8280/actuator/health >/dev/null && break || sleep 4
     done
   fi
   ```
   (Use the default `docker-compose.yml` with Keycloak if the suite exercises protected endpoints.)

2. Make sure `:common` is published to local Maven — `:api-test` depends on it:
   ```bash
   ./gradlew :common:publishToMavenLocal
   ```

3. Run the suite:
   ```bash
   ./gradlew :api-test:integrationTest 2>&1 | tee /tmp/renovation-api-test.log
   ```

4. On failure, extract the first failing test's stack trace:
   ```bash
   grep -A 20 "FAILED" /tmp/renovation-api-test.log | head -80
   ```
   Then open the failing test class under `api-test/src/test/kotlin/` and the corresponding endpoint in `:backend`.

5. Tear down **only** if you brought the stack up:
   ```bash
   docker compose down
   ```

## Notes

- `:api-test` uses Rest Assured via `:common`'s `given(port, token)` helper (`common/src/main/kotlin/.../web/Rest.kt`).
- Tests are tagged `@integrationTest` — they will not run as plain unit tests.
- Tokens are obtained through `:common`'s `PasswordGrantTypeAccessToken` / `ClientCredentialsGrantTypeAccessToken`; if Keycloak is down, auth-requiring tests will fail fast.
