# Marketplace Web Standard

ADR-0004 selects Vue 3, TypeScript, Vite, Vue Router, Pinia, Node.js 22 LTS, and
pnpm through Corepack with a committed lockfile.

## Boundaries

- Route-level containers own data loading and permission-aware presentation.
- Reusable components receive typed data and emit user intent; they do not own
  persistence, SSO policy, or install orchestration.
- All API access uses typed clients generated from or aligned to versioned
  contracts. Do not assemble endpoint URLs across components.
- Server authorization is authoritative. Client guards only improve UX.
- Do not use raw HTML or inline event handlers with metadata.

## State

- Keep local UI state local. Use shared client state only when multiple routes
  consume it.
- Treat server/job state as server-owned and reconcile it through typed APIs.
- Preserve distinct loading, success, empty, permission-denied, error, retrying,
  cancelled, rolling-back, and completed states where relevant.
- Theme preference may be locally persisted; credentials and access tokens follow
  the future SSO ADR and must not be placed in persistent browser storage by
  default.

## Accessibility And UX

- Use semantic links/buttons instead of clickable generic containers.
- Every input has an accessible label and error association.
- Dialogs require labelled semantics, focus trapping, Escape behavior, focus
  restoration, and background interaction/scroll control.
- Saved/toggled controls expose state such as `aria-pressed`.
- Keyboard and visible focus behavior are part of acceptance criteria.
- Never rely on color alone for lifecycle or verification status.

## Prototype Use

Reuse visual tokens, hierarchy, card direction, theme direction, and responsive
intent from the supplied prototype. Do not reuse its `innerHTML`, inline handlers,
fixture-derived analytics, modal-only detail architecture, or hard-coded Day 2
behavior as production code.

## Verification Baseline

TASK-001 and TASK-006 must add the exact pnpm format, lint, typecheck,
unit/component, accessibility, build, and E2E commands here after the scripts
exist. Do not claim planned commands have run before scaffolding exists.
