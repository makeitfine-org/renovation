# :api-test

JUnit 5 + Rest Assured suite that exercises the REST + GraphQL surface of a **running stack**.

## Runs against a live stack

This module does **not** start its own services — it assumes backend, info, gateway, and Keycloak are already reachable. Use `/stack-up` first, or let `/run-api-test` handle the lifecycle.

## Depends on

- `:common` — via local Maven (`renovation:common`). Must be published before first build: `./gradlew :common:publishToMavenLocal`.
- Rest Assured request DSL (`given(port, token)` helper from `:common`).

## Run

```bash
# Stack must be up. Then:
./gradlew :api-test:integrationTest

# Single test class:
./gradlew :api-test:integrationTest --tests "SomeApiTest"
```

All tests here are tagged `@integrationTest` — the plain `test` task will not run them.

## Conventions

- One test class per logical API area (e.g., auth, work, about).
- Use `:common`'s `PasswordGrantTypeAccessToken` / `ClientCredentialsGrantTypeAccessToken` to obtain tokens — do not call Keycloak endpoints directly.
- Assertions target the wire shape (JSON/GraphQL response), not internal models.
- Keep data setup isolated; do not rely on ordering between tests.

## Adding a new test

1. Match the naming + package layout of the nearest existing test.
2. Run it in isolation first: `./gradlew :api-test:integrationTest --tests "YourTest"`.
3. Add the full-suite run to verify no cross-test interference.
