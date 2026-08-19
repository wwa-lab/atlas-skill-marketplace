# Data Flow: RPGLE Skill Marketplace Pilot

## Status and Sources

Accepted for detailed design on 2026-08-18. Sources: RPGLE pilot specification,
ADR-0004, and the slice architecture.

## Boundary Rule

Registry records express requested and reconciled state. Only the trusted local
installer may mutate an authorized Host root, and only a verified installer
terminal result can establish local success.

## Discover and Detail

```text
Browser → Marketplace Web → Registry API → Identity/GitHub authorization
                                      └→ Catalog query → H2/Oracle
Browser ← sanitized access-aware DTO ← authorization-filtered projection
```

Search input is normalized and alias-expanded by the Catalog module. The query
returns only rows allowed by the visibility decision; Restricted/Hidden rows are
filtered before DTO construction. Ordinary inaccessible rows expose only the
approved limited projection. Detail follows the same authorization path and does
not trust a prior search response.

## Publish and Repository Synchronization

```text
Owner → registration request → API → GitHub permission adapter
                              → metadata/tag validation
                              → ownership confirmation
                              → Skill/Version/Artifact records + audit

GitHub webhook ─┐
Owner refresh ──┴→ authenticated sync request → durable RepositorySync
                                           → fetch immutable revision metadata
                                           → validate → publish/reject + audit
```

Webhook bodies are authenticated, schema validated, replay protected, and
reduced to safe identifiers. Metadata is sanitized and stored as registry fields;
source content is not persisted. A new published version creates an Uncertified
certification state and triggers eligibility recomputation.

## Install / Update / Rollback / Uninstall

```text
User intent + scope + version
        │
        ▼
API authorization + eligibility + idempotency
        │
        ▼
LifecycleJob(REQUESTED) → installer operation envelope
        │                         │
        │                         ▼
        │               consent + preflight + staging
        │               integrity + activate + Host check
        │                         │
        ◄──── versioned progress/result ────┘
        │
        ▼
reconcile terminal result → Installation/Backup projection + Audit
```

The operation envelope carries no executable command. Duplicate operation IDs
return the durable result. Update preserves the verified previous version before
activation; failure after activation begins enters rollback. Uninstall targets
one installation scope and may remove only installer-managed paths.

### Lifecycle Error Cascade

```text
validation/auth failure → reject without job or local mutation
adapter unavailable      → job remains retryable; no success projection
integrity failure        → quarantine staged target; current remains active
activation/load failure  → restore and validate previous; report rolled back
result delivery failure  → installer journal remains authoritative; reconcile
```

## Feedback and Certification

```text
User → safe feedback DTO → API validation/redaction → Feedback + Audit
                                        │
                                        └→ independence policy evaluation
                                             └→ threshold reached
                                                  → Certification SUSPENDED
Owner → reply/status transition → authorization → append reply/update status
```

Feedback automatically binds server-resolved Skill Version and installation
scope. The body accepts plain text but rejects upload fields and secret-like
unsupported content. Counting is version-specific and audit-explainable; the
exact independence policy remains blocked by OQ-008.

## Cross-Boundary Object Summary

| Object | Producer | Consumer | Validation / privacy rule |
|---|---|---|---|
| API request/response v1 | Web / API | API / Web | OpenAPI schema; safe error envelope |
| Repository metadata v1 | GitHub adapter | Publication | schema, tag match, sanitized fields |
| Artifact manifest v1 | Artifact adapter | API/Installer | provenance, host, size, integrity |
| Lifecycle operation v1 | API | Installer | consent, idempotency, scope, capability |
| Lifecycle result v1 | Installer | API | operation binding, state transition, redaction |
| Audit context v1 | Application modules | Audit store | allowlisted fields only |

## Refresh and Consistency Strategy

- Catalog queries read committed registry projections; GitHub is not called in
  the synchronous search path.
- Repository changes enter through webhook/manual synchronization and expose the
  last successful revision plus freshness status.
- Lifecycle job UI polls with bounded backoff until the installer protocol ADR
  selects push capability; visibility changes cause immediate server-side
  reauthorization on every poll.
- Optimistic concurrency rejects stale mutable updates; idempotency resolves
  duplicate commands.

## Blocked Flows

Real SSO/GitHub authorization, browser-to-installer handoff, artifact download,
Host loading, certification scoring, and installer self-update cannot be treated
as validated until their corresponding OQs and ADR/contracts are resolved.
