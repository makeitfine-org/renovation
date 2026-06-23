# :frontend-info

Standalone Vue 3 SPA that talks directly to the `:info` GraphQL endpoint. **Not bundled** into any backend — deployed as its own static site.

## Stack — note the differences from `:frontend`

| Dependency | :frontend | :frontend-info |
|---|---|---|
| Build tool | Vite 8 | Vite 8 |
| Axios | 1.7.x | 1.x |
| OAuth | none — session cookie via gateway | **keycloak-js 18.0.1** direct |

Other libs match: Vue 3.2.26, Vue Router 4, Vuex 4, Bootstrap 5.3.

## Auth flow

This SPA drives Keycloak directly via `keycloak-js`. Every GraphQL request attaches the bearer token through an axios interceptor — see `src/plugins/` or `src/services/` (match the existing pattern, do not re-invent).

## Scripts

```bash
cd frontend-info
npm ci
npm run serve   # Vite dev server
npm run build   # outputs to frontend-info/dist/
npm run preview # preview production build locally
npm run clean
```

No Gradle `copyDistToPublic` equivalent here — this SPA ships standalone.

## Vite specifics

- Entry point: `index.html` at project root (not in `public/`)
- `@` alias resolves to `src/` (configured in `vite.config.js`)
- Environment variables: use `VITE_` prefix, accessed via `import.meta.env.VITE_*` (not `process.env.VUE_APP_*`)
- Dev server proxy for `/graphql` and `/worker` in `vite.config.js`
- Build output: `dist/{index.html, favicon.ico, assets/*}` (not `css/` + `js/` like Vue CLI)
- Always use explicit file extensions in imports (`.vue`, `.js`)

## Conventions

Same as `:frontend` (see `rules/vue-components.md`). Prefer Composition API + `<script setup>` for new components; reuse the existing axios + keycloak wiring.

## Do not

- Hardcode the GraphQL endpoint URL in components — use the runtime config wiring.
- Add the Keycloak dependency into `:frontend`; the two SPAs authenticate differently by design.
