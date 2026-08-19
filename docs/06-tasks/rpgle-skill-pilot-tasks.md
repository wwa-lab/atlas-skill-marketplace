# Implementation Task Breakdown: RPGLE Skill Marketplace Pilot

## Status

Accepted as the implementation backlog on 2026-08-18. Tasks explicitly marked
Blocked are not authorized for implementation until their named prerequisite is
accepted.

## Overview

Build the pilot as vertical slices across Vue 3, Spring Boot, relational storage,
versioned contracts, and external adapters. The delivery objective is the full
MVP pilot lifecycle; Day 2 capabilities remain excluded.

Planning assumptions:

- [USER-STATED] Zulu 21.0.8, Spring Boot, Vue 3, H2 local, Oracle dev.
- [ASSUMPTION] Database-backed durable jobs are sufficient for pilot volume.
- Deterministic fake adapters are allowed only in local/test profiles.
- Real cross-boundary adapters remain blocked by OQ-002 through OQ-010.

## Workstreams and Sequencing

1. Foundation: runtime scaffolding, contracts, database baseline, security/error
   conventions, and CI verification.
2. Consumer vertical slice: Discover/Detail, then fake-backed Install/My Skills.
3. Owner vertical slice: registration/publication/sync.
4. Lifecycle/quality: update/rollback/uninstall, feedback/certification.
5. Real adapters, full security/contract tests, Oracle gate, E2E, deployment docs.

Frontend foundations and backend schema/contracts can proceed in parallel after
TASK-001. Real adapters may proceed in parallel only after their ADRs/contracts.

## Task Summary by Domain

### Foundation

- TASK-001–TASK-006: scaffold, contracts, persistence, security, observability,
  and automated verification.

### Consumer and Owner Features

- TASK-007–TASK-016: Discover/Detail, lifecycle, publication/sync, feedback, and
  certification in end-to-end increments.

### Integrations, Verification, and Delivery

- TASK-017–TASK-024: resolve blockers, implement real adapters, Oracle/security/
  E2E verification, deployment, and README.

## Task Details

### TASK-001: Scaffold backend and frontend runtimes

- **Objective**: Create the minimal runnable Spring Boot/Maven API and Vue 3/
  TypeScript/Vite web app using ADR-0004 versions and conventional layouts.
- **Scope**: Java release 21, Zulu 21.0.8 documentation/toolchain check, Node 22
  LTS, pnpm lockfile, health endpoint, web shell, unit test runners, format/lint/
  typecheck scripts, and local environment configuration.
- **Dependencies**: None
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: API tests/build and web lint/typecheck/tests/build pass;
  no business fixture is presented as live data.
- **Traceability**: ADR-0004; Architecture Deployment; Design Scope.

### TASK-002: Establish versioned contracts and safe error handling

- **Objective**: Make OpenAPI v1 and cross-boundary schemas the contract source.
- **Scope**: API error envelope, correlation, pagination, unknown-field policy,
  idempotency header semantics, metadata/manifest/lifecycle/capability schema
  skeletons, and contract lint/breaking-change checks.
- **Dependencies**: TASK-001
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: Contract tests cover valid, invalid, unsupported-version,
  unknown sensitive field, and safe-error cases.
- **Traceability**: CCR-001–CCR-005; API guide Contract Conventions.

### TASK-003: Implement portable persistence baseline

- **Objective**: Create Flyway schema and JPA persistence adapters compatible
  with H2 local and Oracle dev.
- **Scope**: Entities in the data model, application UUIDs, string enums, UTC
  timestamps, optimistic versions, constraints/indexes, local H2 profile, dev
  Oracle profile, and migration/persistence contract suite.
- **Dependencies**: TASK-001, TASK-002
- **Owner type**: backend
- **Priority**: Must
- **Definition of done**: Fresh and upgrade migrations plus repository tests pass
  on H2; no H2 compatibility mode/native query/vendor-generated ID is used.
- **Traceability**: ADR-0004; Data Model; SPR-003.

### TASK-004: Implement application security and authorization seams

- **Objective**: Establish Spring Security, CSRF/session boundary, role/object
  authorization ports, and local-only fixture identity.
- **Scope**: Deny-by-default rules, hidden-resource 404 behavior, limited catalog
  projection, local profile guard, redaction, and authorization matrix tests.
- **Dependencies**: TASK-001, TASK-002
- **Owner type**: security + backend
- **Priority**: Must
- **Definition of done**: Unauthenticated, forbidden, limited, hidden, and allowed
  tests pass; local fake startup fails outside local/test profiles.
- **Traceability**: FR-036–FR-039; SPR-001; Design Identity and Access.

### TASK-005: Implement audit and observability foundation

- **Objective**: Provide transactional audit intents, safe structured logging,
  metrics, health/readiness, and correlation.
- **Scope**: Audit allowlist, append-only adapter, redaction tests, API/job latency
  and outcome metrics, database/migration health.
- **Dependencies**: TASK-003, TASK-004
- **Owner type**: backend + devops
- **Priority**: Must
- **Definition of done**: Protected command audit tests and forbidden-content log/
  metric tests pass.
- **Traceability**: FR-039; SPR-003–SPR-004; Architecture Observability.

### TASK-006: Establish continuous verification

- **Objective**: Run exact backend/frontend/contract checks on every change.
- **Scope**: Java/JDK check, Maven format/static analysis/test/verify, pnpm lint/
  typecheck/test/build, contract tests, diff checks, and coverage reporting with
  80% initial executable-code target without waiving safety transitions.
- **Dependencies**: TASK-001–TASK-005
- **Owner type**: devops + QA
- **Priority**: Must
- **Definition of done**: Clean checkout pipeline is green and commands are added
  to repository standards and README.
- **Traceability**: Testing standard; constitution verification rules.

### TASK-007: Deliver Discover catalog vertical slice

- **Objective**: Search/filter access-aware published Skills from Vue through API
  to persistence.
- **Scope**: taxonomy aliases, managed category filter, deterministic rating/
  certification/relevance order, pagination, card projection, loading/error/empty/
  limited states, accessible keyboard behavior, and visual fidelity to the
  prototype's navigation, Hero, filters, cards, tokens, and responsive layout.
- **Dependencies**: TASK-002–TASK-006
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: FR-001–FR-006 scenarios and search P95 test harness pass;
  hidden metadata never appears; desktop/mobile visual inspection confirms the
  prototype hierarchy without copying fixture metrics or Day 2 sections.
- **Traceability**: US-RPGLE-001; FR-001–FR-006; SC-004.

### TASK-008: Deliver Skill Detail vertical slice

- **Objective**: Show curated RPGLE evidence, versions, access, owner, and
  certification without leaking restricted data.
- **Dependencies**: TASK-007
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: FR-007–FR-008 and limited/hidden/error/accessibility
  cases pass; Detail P95 harness is below the specified budget excluding GitHub;
  Detail uses the same prototype token and panel system as Discover.
- **Traceability**: US-RPGLE-001; FR-007–FR-008.

### TASK-009: Implement lifecycle domain and job reconciliation

- **Objective**: Implement installation/job/backup state machines, idempotency,
  leasing, progress/result reconciliation, and invalid-transition protection.
- **Scope**: Fake installer adapter only; no local file mutation.
- **Dependencies**: TASK-002–TASK-006
- **Owner type**: backend
- **Priority**: Must
- **Definition of done**: Unit/integration tests cover duplicate, conflict,
  concurrency, lease expiry, interruption, terminal replay, rollback, and stale result.
- **Traceability**: FR-009–FR-022, FR-041; SPR-004; Data Model states.

### TASK-010: Deliver fake-backed Install and operation UI

- **Objective**: Complete scope/version/risk confirmation, create/replay operation,
  durable status, retry, and terminal invocation guidance using the fake adapter.
- **Dependencies**: TASK-008, TASK-009
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: Personal default, Project validation/precedence, older
  eligible versions, warnings, failure/retry, focus/dialog, and operation states pass.
- **Traceability**: US-RPGLE-002; FR-009–FR-015; SPR-006.

### TASK-011: Deliver My Skills and update availability

- **Objective**: Group one Skill with independent Personal/Project installations
  and truthful latest/update/certification/action state.
- **Dependencies**: TASK-009, TASK-010
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: FR-015–FR-017 and scope isolation tests pass.
- **Traceability**: US-RPGLE-003.

### TASK-012: Deliver update, automatic rollback, manual rollback, and uninstall

- **Objective**: Implement registry orchestration and UI for safe lifecycle
  outcomes with the deterministic fake adapter.
- **Dependencies**: TASK-009, TASK-011
- **Owner type**: backend + frontend + QA
- **Priority**: Must
- **Definition of done**: Every specified technical failure restores prior state;
  one-backup policy, suppression until higher version, interruption, retry, and
  scope-safe uninstall tests pass.
- **Traceability**: FR-018–FR-022, FR-041; SC-003; SC-007.

### TASK-013: Deliver owner registration and ownership confirmation

- **Objective**: Capture repository/owner/maintainer intent and confirmation with
  permission port and audit.
- **Dependencies**: TASK-004–TASK-006
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: Missing permission, invalid identity, expiry, duplicate,
  confirmation, and dual-consent transfer cases pass with a fake GitHub adapter.
- **Traceability**: FR-023, FR-035.

### TASK-014: Deliver metadata validation, publication, and sync

- **Objective**: Validate schema/tag/revision/artifact evidence, publish atomically,
  reset certification, and support idempotent webhook/manual sync using fakes.
- **Dependencies**: TASK-002–TASK-006, TASK-013
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: FR-024–FR-030 scenarios, webhook replay/out-of-order,
  repository unavailable, and new-version Uncertified tests pass.
- **Traceability**: US-RPGLE-005; SC-005.

### TASK-015: Deliver feedback and Owner response lifecycle

- **Objective**: Submit server-bound safe feedback and expose status/replies/SLA.
- **Dependencies**: TASK-008, TASK-011, TASK-005
- **Owner type**: backend + frontend
- **Priority**: Must
- **Definition of done**: Source/prompt/upload fields are rejected; version/scope,
  states, replies, SLA, authorization, and privacy tests pass.
- **Traceability**: FR-032–FR-034; SC-006.

### TASK-016: Implement certification and qualifying-report transition seam

- **Objective**: Record version-specific assessments and atomically suspend at
  ten qualifying reports through an independence-policy port.
- **Dependencies**: TASK-014, TASK-015
- **Owner type**: backend + QA
- **Priority**: Must
- **Definition of done**: 0/9/10/11, duplicate/nonqualifying, concurrency, new
  version, reassessment, and audit tests pass using a deterministic policy fake.
- **Traceability**: FR-029–FR-031; SC-005; OQ-008.

### TASK-017: Resolve identity/GitHub and restricted-index decisions

- **Objective**: Produce accepted ADR/contracts for OQ-002 and OQ-009.
- **Dependencies**: None
- **Owner type**: security + platform
- **Priority**: Must
- **Definition of done**: Provider type, login/session, role mapping, permission
  checks, cache/index isolation, invalidation, failure policy, and test environment
  are accepted and traceable.
- **Status**: Blocked pending enterprise/provider evidence.

### TASK-018: Implement real SSO and GitHub adapters

- **Objective**: Replace local fakes in dev with the accepted identity/GitHub design.
- **Dependencies**: TASK-017, TASK-004, TASK-013, TASK-014
- **Owner type**: backend + security
- **Priority**: Must
- **Definition of done**: Provider contract, authorization matrix, token handling,
  webhook, retry/rate-limit, and no-leak integration tests pass.
- **Status**: Blocked by TASK-017.

### TASK-019: Resolve Host, installer, browser-local, and self-update decisions

- **Objective**: Produce OQ-003/OQ-004/OQ-005/OQ-010 evidence, ADRs, and versioned
  capability/operation/result contracts.
- **Dependencies**: TASK-002
- **Owner type**: platform + security
- **Priority**: Must
- **Definition of done**: Official/repeatable Host proof; installer form; consent,
  discovery, resume, expiry, replay, reconciliation; signed self-update and recovery
  are accepted with abuse cases.
- **Status**: Blocked pending Windows/VS Code technical validation.

### TASK-020: Implement and verify the trusted Windows installer

- **Objective**: Build the separate installer and VS Code Host adapter for safe
  install/update/rollback/uninstall.
- **Dependencies**: TASK-019, TASK-009, TASK-012
- **Owner type**: platform + security + QA
- **Priority**: Must
- **Definition of done**: Installer-standard unit/contract/temp-root tests cover
  consent, canonical containment, hostile archives, symlinks, integrity, locking,
  interruption/restart, every rollback point, uninstall, and Host load validation.
- **Status**: Blocked by TASK-019.

### TASK-021: Resolve and implement artifact supply chain

- **Objective**: Accept OQ-006 ADR/manifest and implement authorized immutable
  artifact resolution, verification, retention, and revocation.
- **Dependencies**: TASK-002, TASK-014
- **Owner type**: platform + security
- **Priority**: Must
- **Definition of done**: Provenance/version mismatch, checksum/signature, revoked,
  unauthorized, oversized/malformed, and availability tests pass.
- **Status**: Blocked pending artifact custody decision.

### TASK-022: Define certification evidence and feedback independence

- **Objective**: Accept OQ-007 evaluation protocol and OQ-008 product/domain rule,
  then replace fake evaluators.
- **Dependencies**: TASK-016
- **Owner type**: QA + product + security
- **Priority**: Must
- **Definition of done**: Standard non-sensitive corpus/truth set/scoring and an
  auditable independence/anti-abuse policy with boundary examples are approved.
- **Status**: Blocked pending product/evaluation evidence.

### TASK-023: Run release-quality compatibility and E2E gates

- **Objective**: Verify Oracle compatibility, security, accessibility, performance,
  contracts, and critical live journeys.
- **Dependencies**: TASK-006–TASK-022
- **Owner type**: QA + security + devops
- **Priority**: Must
- **Definition of done**: Same Flyway/persistence suite passes on approved Oracle;
  contract/security/a11y/performance checks pass; Consumer and Owner E2E plus all
  SC-001–SC-007 evidence are recorded.

### TASK-024: Complete deployment assets and operator/developer README

- **Objective**: Provide reproducible local start, test, build, Oracle dev deploy,
  configuration, migration, rollback, health, troubleshooting, and known-blocker
  instructions.
- **Dependencies**: TASK-006, TASK-018, TASK-020–TASK-023
- **Owner type**: devops + backend + frontend
- **Priority**: Must
- **Definition of done**: A clean-machine walkthrough using Zulu 21.0.8 succeeds;
  commands are copied into affected standards; no secret/example credential is
  committed.

## Dependency Plan

Critical path:

`TASK-001 → TASK-002 → TASK-003/004 → TASK-005/006 → TASK-007/008 → TASK-009/010/011/012 → TASK-023 → TASK-024`

Owner path: `TASK-004/005 → TASK-013 → TASK-014 → TASK-018/021 → TASK-023`.
Quality path: `TASK-015 → TASK-016 → TASK-022 → TASK-023`.
Installer path: `TASK-019 → TASK-020 → TASK-023`.

The graph is acyclic. TASK-017, TASK-019, and the decision portion of TASK-021/
TASK-022 can run in parallel with local fake-backed feature work.

## Shared Verification Commands

These commands become authoritative when TASK-001/TASK-006 create the referenced
build files; until then they are planned commands, not claimed results.

```text
java -version
./mvnw verify
corepack pnpm install --frozen-lockfile
corepack pnpm lint
corepack pnpm typecheck
corepack pnpm test
corepack pnpm build
git diff --check -- . ':(exclude).agents/skills/**'
```

Oracle verification uses the same Maven integration-test suite with the `dev`
Oracle profile and externally injected connection settings; the exact command is
added after the build profile and approved Oracle environment exist.

## Risks / Blockers

- Real pilot completion is blocked by TASK-017 and TASK-019–TASK-022.
- H2-only success can conceal Oracle SQL/type/locking defects; TASK-023 is mandatory.
- Installer and Host acceptance cannot be replaced by mocked file-copy success.
- The broad MVP requires strict vertical slicing; partial completion must not be
  represented as full pilot readiness.

## Open Questions

The authoritative unresolved list remains `docs/context/open-questions.md`.
No implementation task may select an answer to OQ-002–OQ-010 without the stated
ADR, contract, research, or evaluation record.
