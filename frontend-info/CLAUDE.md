# :frontend-info

Standalone Vue 3 SPA that talks directly to the `:info` GraphQL endpoint. **Not bundled** into any backend — deployed as its own static site.

## Stack — note the differences from `:frontend`

| Dependency | :frontend | :frontend-info |
|---|---|---|
| Vue CLI | 5.0.8 | **4.5.15** (older) |
| Axios | 1.7.x | **0.24.0** (older) |
| OAuth | none — session cookie via gateway | **keycloak-js 18.0.1** direct |

Other libs match: Vue 3.2.26, Vue Router 4, Vuex 4, Bootstrap 5.3.

## Auth flow

This SPA drives Keycloak directly via `keycloak-js`. Every GraphQL request attaches the bearer token through an axios interceptor — see `src/plugins/` or `src/services/` (match the existing pattern, do not re-invent).

## Scripts

```bash
cd frontend-info
npm ci
npm run serve   # dev server
npm run build   # outputs to frontend-info/dist/
npm run clean
```

No Gradle `copyDistToPublic` equivalent here — this SPA ships standalone.

## Conventions

Same as `:frontend` (see `rules/vue-components.md`). Prefer Composition API + `<script setup>` for new components; reuse the existing axios + keycloak wiring.

## Do not

- Bump `vue-cli` to 5.x independently — it would silently diverge from `:frontend` tooling. Coordinate a joint upgrade.
- Hardcode the GraphQL endpoint URL in components — use the runtime config wiring.
- Add the Keycloak dependency into `:frontend`; the two SPAs authenticate differently by design.
