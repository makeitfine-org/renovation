---
name: stack-down
description: Stop and remove the renovation Docker Compose stack (both with-security and no-security variants). Use when the user says "stop the stack", "docker down", or "shut it down".
allowed-tools: Bash
---

# stack-down

Tear down the renovation local stack.

## Procedure

```bash
docker compose -f docker-compose.yml down --remove-orphans 2>/dev/null || true
docker compose -f docker-compose-no-security.yml down --remove-orphans 2>/dev/null || true
```

Then confirm nothing is left running from this project:
```bash
docker ps --filter "label=com.docker.compose.project=renovation" --format "table {{.Names}}\t{{.Status}}"
```

## Notes

- Do **not** add `-v` (volume removal) by default — that wipes Keycloak realm data, Postgres schema, and Mongo content. Only add `-v` if the user explicitly asks for a clean reset.
- If debug compose was in use, the down on the default compose file already covers it (same project name).
