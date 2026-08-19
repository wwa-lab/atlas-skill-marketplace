# System Architecture: RPGLE Skill Marketplace Pilot

## Status

Accepted for detailed design on 2026-08-18.

## Overview

- **Architecture summary**: Atlas is a centralized registry over Skills stored
  in owner-controlled GitHub repositories. The pilot uses a Vue web application,
  a Spring Boot modular-monolith Registry API, relational persistence, external
  integration adapters, and a separately trusted Windows installer.
- **Design objective**: Close the Discover → Detail → Install → My Skills →
  Update/Rollback → Uninstall and Register → Validate → Publish/Sync journeys
  without moving repository or local-filesystem authority into the browser/API.
- **Architectural style**: Ports-and-adapters modular monolith with separate web,
  API, and local-installer deployables.

## Source Specification

- `docs/03-spec/rpgle-skill-pilot-spec.md`
- `docs/product/Atlas_Marketplace_PRD_v0.3.md`
- ADR-0004

## Architectural Drivers

### Functional Drivers

- Access-aware discovery, detail, certification evidence, and truthful ranking.
- Versioned registration, publication, repository synchronization, and feedback.
- Scope-specific installation lifecycle with durable, reconciled jobs.
- Safe local update, automatic technical rollback, manual quality rollback, and
  uninstall.

### Non-Functional Drivers

- Search P95 below 500 ms and Detail P95 below 1 second, excluding GitHub calls.
- Initial interaction within 3 seconds on the internal network.
- Object-level authorization, metadata sanitization, artifact provenance,
  idempotency, recovery, auditability, and privacy.
- WCAG 2.1 AA user-facing behavior.
- H2 local development with Oracle compatibility verified in dev.

### Constraints and Assumptions

- [USER-STATED] Zulu JDK 21.0.8, Spring Boot, Vue 3, H2 local, Oracle dev.
- Git Tag is Published Version authority; source remains in GitHub.
- The API never treats registry state as local installation success.
- [ASSUMPTION] The initial deployment is single-region and can use database-backed
  durable jobs without a message broker; workers claim jobs with leases.
- Installer protocol, Host behavior, SSO/GitHub mapping, and artifact custody
  remain blocked by OQ-002 through OQ-010 as applicable.

## System Context

### Primary Actors

| Actor | Role |
|---|---|
| Consumer | Discover, evaluate, install, maintain, uninstall, and give feedback |
| Owner / AI SME | Register, publish, synchronize, certify, and respond |
| Administrator | Govern taxonomy, ownership exceptions, visibility, and audit |
| Local installer | Execute consented, constrained filesystem lifecycle work |

### External Systems

| System | Integration Purpose |
|---|---|
| Company identity provider | Authentication and group/role context |
| GitHub Enterprise | Repository authority, tags, metadata, permissions, webhooks |
| Artifact source | Immutable, authorized distribution artifacts |
| VS Code GitHub Copilot Chat | Pilot Host discovery, load validation, invocation |

### System Boundary

Atlas owns registry metadata, publication state, lifecycle orchestration records,
feedback, certification, and audit evidence. GitHub owns source and repository
authorization; the artifact source owns immutable payload custody; the installer
owns authorized local mutation; the Host owns actual Skill loading and execution.

## High-Level Architecture

```text
┌────────────────────────────────────────────────────────────────────┐
│ Employees · Owners · AI SMEs · Administrators                      │
└───────────────────────────────┬────────────────────────────────────┘
                                │ HTTPS
                                ▼
┌────────────────────────────────────────────────────────────────────┐
│ Marketplace Web · Vue 3 · typed API client · accessible UI states │
└───────────────────────────────┬────────────────────────────────────┘
                                │ REST / JSON / OpenAPI
                                ▼
┌────────────────────────────────────────────────────────────────────┐
│ Registry API · Spring Boot modular monolith                        │
├────────────────────────────────────────────────────────────────────┤
│ Catalog & Access │ Publication & Sync │ Lifecycle │ Feedback/Audit │
├────────────────────────────────────────────────────────────────────┤
│ Ports: persistence · identity · GitHub · artifact · installer      │
└──────────────┬──────────────────────┬──────────────────────┬────────┘
               │ JDBC/JPA             │ HTTPS/webhook         │ versioned
               ▼                      ▼                       ▼ contract
┌──────────────────────┐   ┌──────────────────────┐   ┌──────────────────┐
│ H2 local / Oracle dev│   │ SSO · GitHub ·       │   │ Windows Installer│
│ Registry Store       │   │ Artifact Source      │   │ + Host Adapter   │
└──────────────────────┘   └──────────────────────┘   └──────────────────┘
```

## Component Breakdown

### Marketplace Web

- **Discover and Detail**: Search, filters, cards, curated evidence, access and
  certification states; Day 2 controls are disabled and labeled Coming Soon.
- **Lifecycle UI**: Scope/version confirmation, durable job status, retry,
  rollback, uninstall, and invocation guidance.
- **Owner UI**: Registration, validation, publication, refresh, certification,
  and feedback response.
- **Session shell**: SSO-aware navigation, theme, authorization-aware rendering,
  error boundaries, and accessibility behavior.

### Registry API Modules

- **Identity and Access**: Maps authenticated identity and roles, performs
  object-level repository visibility and operation authorization.
- **Catalog**: Owns taxonomy, aliases, sanitized registry projections, search,
  detail, rating truthfulness, and access-aware result shaping.
- **Publication**: Owns registration, ownership confirmation, tag/version rules,
  metadata validation, certification reset, and publication state.
- **Repository Sync**: Accepts authenticated webhooks/manual refresh, validates
  revisions, and records durable outcomes.
- **Lifecycle Orchestration**: Creates idempotent operations, checks eligibility,
  exposes job state, and reconciles signed/authorized installer results.
- **Installation View**: Projects installed/latest/previous concepts per scope
  without claiming local success before verified reconciliation.
- **Feedback and Certification**: Owns safe feedback, reply/status history,
  independent-report counting, and certification transitions.
- **Audit and Observability**: Append-only business audit, correlation, safe
  structured logs, metrics, and health signals.

### Integration Adapters

- Identity, GitHub, artifact, installer transport, Host capability, clock, and
  persistence are ports with environment-specific adapters.
- Local development uses deterministic fake adapters and H2; fake authorization
  is never enabled in dev deployments.

## Data Architecture

### Conceptual Entities

| Aggregate / Entity | Responsibility |
|---|---|
| Skill / SkillVersion | Catalog identity and immutable published releases |
| Certification | Version-specific assessment and suspension state |
| DistributionArtifact | Provenance, compatibility, integrity, availability |
| Installation / Backup | Scope-specific reconciled local state and one prior version |
| LifecycleJob | Idempotent requested operation and durable progress/result |
| Feedback / FeedbackReply | Version/scope report and owner response history |
| RepositorySync | Revision validation and publication eligibility |
| OwnershipConfirmation | Owner/maintainer acceptance and transfer evidence |
| AuditEvent | Append-only, privacy-filtered security/business evidence |

### Persistence Strategy

- Spring Data JPA repositories are persistence adapters, not domain APIs.
- Flyway migrations are the schema authority for H2 and Oracle.
- Application-generated UUIDs avoid database-specific identity behavior.
- Optimistic versions protect mutable aggregates; unique idempotency keys and
  job leases protect lifecycle execution.
- Audit rows are append-only; sensitive payloads are not serialized into generic
  JSON columns.

## Integration Architecture

| Boundary | Pattern | Fail-closed behavior |
|---|---|---|
| Web → API | HTTPS REST/JSON, `/api/v1`, session/CSRF policy | Reject invalid schema/session/object access |
| GitHub → API | Authenticated webhook plus manual refresh | Reject invalid signature/replay/schema |
| API → GitHub | Narrow adapter calls with service credential/user context | No metadata/artifact leak on uncertain permission |
| API → Artifact source | Authorized immutable manifest resolution | No install job without provenance/integrity |
| API ↔ Installer | Versioned operation/result contract | No arbitrary command; unsupported capability fails before mutation |
| Installer → Host | Allowlisted Host adapter | No success until post-install load validation |

## Workflow and State Architecture

- Publication: `DRAFT → PUBLISHED → DEPRECATED → ARCHIVED`; only validated tags
  with confirmed ownership can enter `PUBLISHED`.
- Certification: `UNCERTIFIED → CERTIFIED | FAILED`; `CERTIFIED → SUSPENDED` on
  the accepted independent-feedback rule; a new version starts `UNCERTIFIED`.
- Installation: `NOT_INSTALLED → INSTALLING → INSTALLED`; update uses
  `UPDATING → INSTALLED | ROLLING_BACK → INSTALLED | INCOMPLETE`; uninstall uses
  `UNINSTALLING → UNINSTALLED | INCOMPLETE`.
- Lifecycle jobs are immutable-intent state machines with terminal results;
  retries reuse the operation id and replay/reconcile rather than duplicate work.

## Deployment and Environment

| Concern | Local | Dev |
|---|---|---|
| JDK | Zulu 21.0.8 | Zulu 21.0.8 |
| API database | H2 file or in-memory profile | Oracle (exact supported version pending environment record) |
| External integrations | Deterministic fakes | Approved SSO/GitHub/artifact adapters when ADRs land |
| Web | Vite development server | Static artifact behind approved internal web ingress |
| API | Spring Boot process | Independently deployable Spring Boot artifact |

Secrets are injected at runtime. Database migrations run as a controlled deploy
step with least-privilege credentials; application credentials do not receive
schema-owner privileges where the platform permits separation.

## Security, Reliability, and Observability

- Server-side authentication and per-object authorization are mandatory for all
  protected operations; frontend guards are not evidence.
- Metadata is sanitized and rendered without raw HTML. Webhook, API, metadata,
  manifest, and installer inputs are version/schema validated.
- Lifecycle requests require consent, idempotency, capability, immutable artifact
  identity, and integrity binding.
- Structured logs and audit use stable IDs and redaction; source, prompts,
  tokens, artifact content, and unrestricted local paths are excluded.
- Health/readiness distinguish process, database, migration, and external adapter
  status. Metrics cover API latency, authorization denial, job age/outcome,
  synchronization, rollback, and feedback SLA without private content.

## Risks and Trade-offs

| Risk | Treatment |
|---|---|
| H2 differs from Oracle | Portable mappings plus mandatory Oracle integration gate |
| Installer/Host behavior unproven | Block lifecycle execution adapter tasks on OQ-003–OQ-005 |
| SSO/GitHub mapping unresolved | Use ports/fakes locally; block real auth deployment |
| Artifact custody/signing unresolved | No real install artifact until ADR and manifest contract |
| Large pilot scope | Implement vertical slices and keep Day 2 behavior disabled |
| Database-backed job throughput | Adequate for pilot assumption; measure before adding messaging |

## Open Questions / Implementation Blockers

1. OQ-002: GitHub Enterprise and SSO mapping.
2. OQ-003–OQ-005: Host evidence, installer form, and browser/local protocol.
3. OQ-006: Artifact construction, custody, integrity, and revocation.
4. OQ-007–OQ-008: Certification evidence and independent-feedback policy.
5. OQ-009–OQ-010: restricted indexing and installer self-update.

These block their adapters or acceptance tests, not catalog/API scaffolding and
local fake-backed vertical slices.

