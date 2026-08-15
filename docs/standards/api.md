# Registry API Standard

This standard remains framework-neutral until stack and deployment ADRs exist.

## Layering

```text
transport/API -> application use cases -> domain -> ports
                                      -> adapters (persistence, GitHub, SSO,
                                                   artifacts, job runner)
```

- Transport code validates and maps requests; it does not implement persistence
  queries or lifecycle orchestration inline.
- Application use cases enforce business policy and explicit state transitions.
- Domain code does not depend on HTTP framework, database, GitHub SDK, or local
  installer transport types.
- Adapters isolate vendor and deployment choices behind typed ports.

## Contracts And Validation

- Validate requests, GitHub payloads, metadata, manifests, and client events with
  versioned schemas at their first trusted boundary.
- Use stable machine error codes, a user-safe message, and a correlation/request
  identifier. Do not expose secrets, source content, tokens, stack traces, or raw
  provider errors.
- Define idempotency keys and optimistic/concurrency semantics for lifecycle and
  publication operations.
- Keep API DTOs separate from persistence models.

## Authorization And Data

- Authenticate through company SSO after ADR selection and authorize every
  protected operation at the API and object level.
- Reconcile repository visibility with GitHub permissions; registry presence
  never grants source access.
- Persist only registry-relevant metadata and structured operation results.
- Schema changes use the migration tool selected with the persistence ADR.

## Async Work And Audit

- GitHub sync and lifecycle orchestration are durable asynchronous jobs when they
  can outlive a request.
- Job transitions are explicit, validated, idempotent, observable, and audited.
- Structured logs include correlation id and stable identifiers, not private
  content or unrestricted paths.

## Verification Baseline

When scaffolding exists, document exact format, lint, typecheck, unit, integration,
contract, migration, security, and build commands here.
