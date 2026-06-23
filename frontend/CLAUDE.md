# :frontend

Vue 3 SPA for the main renovation backend — **bundled into the backend JAR** as static resources.

## Stack

- Vue **3.2.26**
- **Vite 8** (migrated from Vue CLI 5)
- Vue Router 4, Vuex 4
- Bootstrap 5.3
- Axios 1.7

## Scripts

```bash
cd frontend
npm ci
npm run serve    # Vite dev server (proxy config in vite.config.js)
npm run build    # outputs to frontend/dist/
npm run preview  # preview production build locally
npm run clean    # remove dist + node_modules
```

## Gradle integration

- `./gradlew :frontend:npmBuild` — calls `npm run build`
- `./gradlew :backend:copyDistToPublic` — copies `frontend/dist/` → `backend/src/main/resources/public/` (automatic on `:backend:build`)
- Do **not** edit files under `backend/src/main/resources/public/` or `frontend/dist/` — they are regenerated.

## Vite specifics

- Entry point: `index.html` at project root (not in `public/`)
- `@` alias resolves to `src/` (configured in `vite.config.js`)
- Environment variables: use `VITE_` prefix, accessed via `import.meta.env.VITE_*` (not `process.env.VUE_APP_*`)
- Dev server proxy for `/api` and `/logout-without-redirect` in `vite.config.js`
- Build output: `dist/{index.html, favicon.ico, assets/*}` (not `css/` + `js/` like Vue CLI)

## Conventions (`rules/vue-components.md`)

- Composition API with `<script setup>` for new components.
- Bootstrap 5.3 utility classes; minimal inline styles.
- Vuex 4 modules under `src/store/modules/`; mutations sync, side effects in actions.
- Use the shared axios instance; don't `import axios` fresh in components.
- Calls target same-origin `/api/*` — no CORS gymnastics needed.
- Always use explicit file extensions in imports (`.vue`, `.js`).

## Tests

No JavaScript test runner is currently wired in `package.json`. Do not add new components depending on runtime tests without also adding a test framework (coordinate first).

## Linting

No ESLint/Prettier configured at the moment. If you introduce one, add the corresponding Gradle task to `npm run build` / pre-push.
