---
name: vue-components
description: Vue 3 + Bootstrap 5 conventions for the frontend and frontend-info SPAs.
---

# Vue Component Rules

Both SPAs use **Vue 3.2.26** + **Vuex 4** + **Vue Router 4** + **Bootstrap 5.3**. They differ in:
- `frontend/` — Vue CLI 5, Axios 1.7, bundled into backend JAR via the Gradle `copyDistToPublic` task.
- `frontend-info/` — Vue CLI **4.5** (older), Axios 0.24, `keycloak-js` 18.0.1 for OAuth.

**Component style**
- Prefer **Composition API with `<script setup>`** for new components.
- Use Bootstrap 5.3 utility classes for layout/spacing — avoid inline `style=""` unless truly one-off.
- Colocate component-scoped CSS with `<style scoped>`.
- Props: declare with `defineProps` + types; avoid untyped `Object` props.

**State**
- Vuex 4: modules live in `src/store/modules/`. Keep mutations synchronous; do async in actions.
- Do not read the store directly from a template — go through a computed.

**API calls**
- Use the module's existing axios instance (do not `import axios` fresh in each component).
- The `frontend` SPA talks to `/api/*` (same-origin, bundled into backend).
- The `frontend-info` SPA talks directly to Info's GraphQL endpoint — attach the Keycloak bearer token via request interceptor.

**Testing**
Neither SPA currently has a test runner wired in `package.json`. Do not add new components that require runtime tests unless you also add a test framework (coordinate before doing so).

**Build**
```bash
cd frontend && npm run build   # outputs to frontend/dist/, consumed by :backend
```
Don't edit files under `frontend*/dist/` or `frontend*/node_modules/`.
