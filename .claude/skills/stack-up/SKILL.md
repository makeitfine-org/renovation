---
name: stack-up
description: Start the renovation local stack via docker compose, wait for health, and print the port map. Use when the user says "start the stack", "bring up docker", or "start local environment".
allowed-tools: Bash, Read
---

# stack-up

Bring up the full renovation Docker Compose stack for local development.

## Procedure

1. Ask the user which variant they want (only if unclear from context):
   - **With Keycloak (default):** `docker-compose.yml`
   - **No security:** `docker-compose-no-security.yml`
   - **Debug ports (JDWP):** `docker-compose.yml` + `docker-compose-debug.yml`

2. Verify `.env` exists at project root (it provides variable substitution for Compose). If missing, stop and tell the user.

3. Start the stack:
   ```bash
   docker compose up -d                  # default
   # or:
   docker compose -f docker-compose-no-security.yml up -d
   # or (debug):
   docker compose -f docker-compose.yml -f docker-compose-debug.yml up -d
   ```

4. Wait up to 120 s for backend + info to report healthy:
   ```bash
   for i in $(seq 1 30); do
     curl -sf http://localhost:8280/actuator/health >/dev/null && break || sleep 4
   done
   ```

5. Print the service port map:
   - Backend:        `http://localhost:8280`  (Swagger: `/swagger`, Actuator: `/actuator/health`)
   - Info (GraphQL): `http://localhost:9190/graphql`
   - Gateway:        `http://localhost:8285`
   - Keycloak:       `http://localhost:18080`
   - PostgreSQL:     `localhost:5532` (user `postgres`, db `postgres`, schema `renovation`)
   - MongoDB:        `localhost:27117` (db `infodb`)
   - Redis:          `localhost:6479`
   - Prometheus / Grafana / Jaeger / Loki: see `docker-compose.yml`

## Notes

- If `docker compose up` fails on port conflicts, suggest running `/stack-down` first.
- Startup order matters: Keycloak → backend/info/gateway. Don't hit the APIs before step 4 succeeds.
