# :frontend

Vue 3 SPA for the main renovation backend — **bundled into the backend JAR** as static resources.

## Stack

- Vue **3.2.26**
- Vue CLI **5.0.8** (not Vite)
- Vue Router 4, Vuex 4
- Bootstrap 5.3
- Axios 1.7
- Node requires `--openssl-legacy-provider` (already wired in npm scripts)

## Scripts

```bash
cd frontend
npm ci
npm run serve    # local dev server (proxy config in vue.config.js)
npm run build    # outputs to frontend/dist/
npm run clean    # remove dist + node_modules
```

## Gradle integration

- `./gradlew :frontend:npmBuild` — calls `npm run build`
- `./gradlew :backend:copyDistToPublic` — copies `frontend/dist/` → `backend/src/main/resources/public/` (automatic on `:backend:build`)
- Do **not** edit files under `backend/src/main/resources/public/` or `frontend/dist/` — they are regenerated.

## Conventions (`rules/vue-components.md`)

- Composition API with `<script setup>` for new components.
- Bootstrap 5.3 utility classes; minimal inline styles.
- Vuex 4 modules under `src/store/modules/`; mutations sync, side effects in actions.
- Use the shared axios instance; don't `import axios` fresh in components.
- Calls target same-origin `/api/*` — no CORS gymnastics needed.

## Tests

No JavaScript test runner is currently wired in `package.json`. Do not add new components depending on runtime tests without also adding a test framework (coordinate first).

## Linting

No ESLint/Prettier configured at the moment. If you introduce one, add the corresponding Gradle task to `npm run build` / pre-push.
