# Detailed Design: RPGLE Skill Marketplace Pilot

## Status

Accepted for task decomposition on 2026-08-18, with the blockers listed under
Open Questions.

## Overview

This design turns the accepted pilot specification and architecture into
implementation-facing modules, interfaces, workflows, state rules, UI behavior,
validation, and test boundaries. No application code exists yet; all referenced
modules are planned, not claims about existing implementation.

## Sources and Decisions

- RPGLE pilot requirements, stories, and specification
- RPGLE pilot architecture, data flow, and data model
- ADR-0004
- Project API, web, installer, testing, and security standards

## Design Scope

In scope: Marketplace Web, Registry API, relational schema, versioned REST API,
fake local adapters, and contracts/tasks for the installer and integrations.
Out of scope: online Skill execution, source hosting/editing, Day 2 features,
unsupported OS/Hosts, silent updates, and implementation of unresolved real
SSO/GitHub/artifact/installer adapters before their ADRs.

## Backend Module Design

### Identity and Access

The transport obtains an authenticated principal from Spring Security. An
application authorization service resolves company role and repository access
through an identity/GitHub port. Every catalog detail, artifact, publication,
feedback-management, and lifecycle use case performs object authorization.
Decisions return `VISIBLE`, `LIMITED`, or `HIDDEN`; `LIMITED` exposes only the
ordinary internal name/description/contact projection.

Local profile uses named fixture principals through an explicit development-only
adapter. Startup fails if that adapter is enabled outside `local` or tests.

### Catalog

Search accepts query, category, page, and size. Query normalization trims,
case-folds, collapses whitespace, and resolves managed aliases. Rated results
sort by rating descending; unrated results sort by certification then relevance;
all branches add display name and UUID as deterministic tie-breakers. Page size
is capped at 50.

Detail returns safe metadata, published versions, curated report, certification,
access state, and allowed actions. Search/detail never synchronously call GitHub
for descriptive content; authorization may use a short-lived permission decision
cache only after OQ-009 defines isolation and invalidation.

### Publication and Sync

Registration captures a repository reference, proposed owner, and primary
maintainer. Publication requires confirmed ownership, verified Write/Maintain
permission, a valid metadata schema, matching metadata version (when present),
an immutable Git Tag/revision, and an eligible artifact manifest. Publishing is
atomic: Skill Version becomes Published, certification becomes Uncertified, and
audit is appended in one transaction.

Webhook and manual refresh share the same sync use case. The delivery/request ID
is idempotent. Out-of-order observed revisions are recorded but cannot overwrite
a newer accepted projection without an explicit reconciliation rule.

### Lifecycle Orchestration

Create-operation validates authorization, scope, version eligibility, risk
acknowledgment, Host capability, artifact provenance, and idempotency. It stores
immutable intent before dispatch. A worker leases ready jobs; duplicate workers
cannot own the same unexpired lease. Progress/result input is bound to operation,
installation, target version, and contract version.

The installation projection changes to Installed only after a verified installer
success result. Rolled-back results keep the previous version installed and store
the failed target/reason. Unknown, expired, or unsupported result contracts fail
closed and retain a reconcilable job state.

### Feedback and Certification

Feedback submission resolves Skill Version and scope server-side. Plain text is
bounded and sanitized for display; attachments and source/prompt fields are not
part of the contract. Owner replies are appended; feedback state transitions are
validated. The independence evaluator is a port until OQ-008 is accepted. Only
qualifying reports increment the version counter; crossing from nine to ten
suspends Certified state atomically and audits the reason.

### Audit and Observability

Application use cases emit structured audit intents in the same transaction as
the protected state change. A strict allowlist builds audit context. HTTP logs
use correlation IDs and stable resource IDs. Metrics contain counts/timing and
bounded codes, never query bodies, feedback text, source, prompts, tokens, signed
URLs, artifacts, or local paths.

## Frontend Module and Route Design

| Route | Responsibility | Primary states |
|---|---|---|
| `/discover` | search, category/filter, accessible results | loading, results, empty, limited, error |
| `/skills/:slug` | evidence, versions, certification, install/feedback intent | loading, hidden/404, no access, ready, warning |
| `/my-skills` | grouped scopes and lifecycle actions | empty, installed, update, incomplete, unavailable |
| `/register` | repository/owner registration and validation | editing, validating, confirmation, invalid |
| `/owner/skills/:slug` | publish/sync/certification/feedback management | ready, syncing, conflict, permission denied |
| `/operations/:id` | durable lifecycle terminal/progress experience | pending, running, success, rolled back, failed, incomplete |

Route containers load server state through one typed client. Reusable components
receive typed props and emit intent. Dialogs trap focus, label title/description,
close with Escape, restore focus, and never rely on color alone. Metadata is
rendered as text or through a sanitized Markdown component without raw HTML.

The prototype supplies visual tokens, card hierarchy, responsive direction, and
theme intent only. Day 2 areas are either absent or disabled with Coming Soon;
fixture ratings/installs/quality numbers are not displayed as live data.

For Discover and Detail, implementation fidelity means preserving the prototype's
70 px translucent navigation, Atlas mark, centered gradient Hero, pill-shaped
filters, 1180 px content container, three-column desktop card grid, compact
typography, bordered gradient panels, metric footer hierarchy, responsive
breakpoints, and complete light/dark token mapping. Dynamic totals and card
metrics MUST come from the API; prototype fixture counts and Day 2 sections MUST
NOT be copied into the pilot UI.

## API and Interface Design

All endpoints use `/api/v1`, JSON, an authenticated session, CSRF protection for
browser mutations, a correlation header, and the error envelope defined in the
API guide. Mutation endpoints support optimistic conflict handling; lifecycle,
sync, and webhook operations additionally require idempotency identifiers.

Domains: session, catalog/detail, installations/jobs, registration/publication/
sync, feedback/certification, and audit/admin. Exact endpoints and payloads are
defined in the companion API implementation guide.

## Workflow Design

### Discover to Detail

1. Normalize query/filter and request first page.
2. API authorizes and returns access-shaped results with deterministic sort.
3. Web renders results or actionable empty state.
4. Detail request independently reauthorizes and returns full, limited, or hidden.

### Install and Resume

1. Web gets eligible versions/capability and opens an accessible confirmation.
2. User selects scope/version and acknowledges any risk.
3. API creates or replays the idempotent job.
4. If installer readiness is absent, web displays guided setup while preserving
   operation intent; actual discovery/resume awaits the protocol ADR.
5. Installer reports progress/terminal evidence; API reconciles.
6. Success page shows only a Host-validated invocation command.

### Update and Rollback

1. User reviews changelog, target, certification, compatibility, and scope.
2. Installer preflights, stages, verifies, preserves one prior version, activates,
   and validates Host loading.
3. Failure after activation restores and validates previous version.
4. Manual rollback restores previous and suppresses rejected target until a
   strictly greater version is eligible.

### Uninstall

Confirmation identifies exact Skill, Host, scope, and installed version. The
installer deletes only managed verified content for that scope. Interrupted work
returns Incomplete and remains retryable; it is never projected as Uninstalled.

## Validation and Error Handling

| Boundary | Required validation |
|---|---|
| HTTP | schema, lengths, enum, pagination, CSRF, auth, object authorization |
| Metadata/tag | schema version, tag/version match, sanitized fields, repository permission |
| Webhook | signature, delivery ID, replay, schema, repository binding |
| Artifact | immutable identity, authorization, size/format, checksum/signature, Host compatibility |
| Lifecycle | contract version, consent, idempotency, scope, state transition, capability |
| Feedback | server-bound version/scope, plain text bounds, no upload/source/prompt fields |

Errors use stable codes and safe messages. `400` is malformed input, `401` unauthenticated,
`403` known-but-forbidden operation, `404` includes hidden resources to prevent
enumeration, `409` is stale/invalid transition/idempotency conflict, `422` is a
well-formed domain validation failure, `429` is rate control, and `503` is a
temporarily unavailable dependency. Stack traces/provider payloads are never returned.

## Database and Transaction Design

- Flyway migrations precede application startup schema validation.
- Each application command owns one transaction around aggregate change and
  audit append; remote calls occur before or after with durable intent rather
  than holding database locks.
- Pessimistic locking is limited to job claiming where Oracle/H2 behavior is
  covered by adapter tests; aggregate updates use optimistic locking.
- Portable JPQL/criteria queries are the default. Any native SQL requires an
  adapter decision and dual-engine verification.

## Security, Reliability, and Privacy

- SameSite/secure session cookies and CSRF follow the SSO ADR; tokens are not put
  in local storage.
- Restricted/Hidden resources use non-enumerating responses and authorization-
  filtered search/index construction.
- Lifecycle and publication rate limits, idempotency, leases, and audit protect
  replay/concurrency paths.
- All external adapter responses are treated as untrusted.
- Backup, retry, rollback, and reconciliation are normal states, not exception-only
  logging behavior.

## Testing Considerations

- Domain unit tests cover every valid/invalid state transition and version rule.
- H2 integration tests cover migrations, repositories, transactions, optimistic
  conflicts, and API behavior; the same persistence/migration contract suite runs
  against Oracle before dev deployment.
- Web tests cover components, keyboard/focus, empty/error/permission/warning states,
  and sanitized rendering.
- Contract tests cover OpenAPI, metadata, manifest, lifecycle, Host capability,
  webhook, and backward/unsupported schema behavior.
- Security tests cover object authorization, enumeration, CSRF/XSS, injection,
  secret redaction, replay, and hostile lifecycle inputs.
- E2E covers both critical consumer and owner journeys once real adapters exist.

## Risks and Trade-offs

- Fake local adapters allow early vertical slices but are never acceptance
  evidence for GitHub, SSO, installer, artifact, or Host behavior.
- Database-backed jobs simplify the pilot but require lease/age monitoring.
- Full pilot breadth should be delivered in vertical slices; task priority does
  not promote Day 2 features.

## Open Questions / Blockers

- OQ-002 through OQ-010 remain as recorded. Tasks crossing those boundaries are
  blocked until their ADR, contract, or evaluation protocol is accepted.
- The exact Oracle dev version and managed driver distribution must be recorded
  before Oracle verification/deployment.
