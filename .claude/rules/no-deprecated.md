---
name: no-deprecated
description: Hard ban on deprecated APIs; always verify via MCP docs before using any library call.
---

# No Deprecated Code

The root `CLAUDE.md` "Documentation First" section is the law. Summary:

1. **Before calling any library API**, query its docs via the appropriate MCP server. Fallback: Context7 MCP (`mcp__context7__resolve-library-id` → `mcp__context7__query-docs`).
2. **Never use deprecated methods/classes/annotations**, even if they still compile.
3. **Verify version-specific signatures** against the current docs — especially for Spring Boot, DGS, and Vue.

## Examples — don't use

| Deprecated | Replace with |
|---|---|
| `@Autowired` on fields | constructor injection |
| `WebMvcConfigurer.addCorsMappings` (old signatures) | current overrides from Spring Boot 3.2 docs |
| `@RequestMapping(method = GET)` | `@GetMapping` |
| Vue 2 `Vue.extend({})` | Vue 3 `defineComponent({})` |
| Vue Options API for new components | Composition API with `<script setup>` |
| DGS `@DgsData(parentType=..., field=...)` (old string form) — verify | current typed overload |
| Spring `AntPathRequestMatcher` for new security config | `HttpSecurity.authorizeHttpRequests` with DSL |
| Reactor `Mono.zipWhen` used in non-reactive code | use the right variant per current docs |

## When in doubt

```
mcp__context7__resolve-library-id  →  mcp__context7__query-docs
```
Do not guess from training data. Spring Boot, Vue, and DGS have all shipped breaking renames in recent minor versions.
