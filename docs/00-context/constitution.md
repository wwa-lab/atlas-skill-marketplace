<!--
Sync Impact Report
- Version change: 1.0.1 -> 1.1.0
- Workflow change: Spec Kit retired; standalone SDD profile and `docs/01`–`docs/06`
  document chain adopted
- Governance source moved from `.specify/memory/constitution.md` to this file
- Dependent rules, skills, README, ADRs, and RPGLE pilot artifacts migrated
- Follow-up: technology stack, local-client protocol, artifact strategy, SSO
  integration, and Host capability remain ADR/research decisions
-->
# Atlas Marketplace Constitution

## Core Principles

### I. Product Scope And Source Of Truth

Atlas Marketplace MUST remain a centralized internal registry and lifecycle
manager over Skills stored in distributed GitHub repositories. It MUST NOT become
a source-code host, online Skill runtime, workflow engine, public marketplace, or
prompt playground without a deliberate constitution and PRD amendment.

For an active change, its approved specification under `docs/03-spec/` is the
source of change intent;
the PRD and recorded product decisions define product scope; ADRs explain
cross-cutting technical choices. A visual prototype is evidence of design intent,
not an implementation contract or an authority to promote Day 2 scope into MVP.

### II. Explicit Trust Boundaries

The Marketplace web app, Registry API, GitHub integration, artifact source, and
local installer MUST be treated as distinct trust boundaries. Browser code and
the Registry API MUST NOT perform unrestricted local shell or filesystem
operations. GitHub remains the final access-control boundary for private source.

Host-specific behavior MUST be isolated behind adapters, and cross-boundary
messages MUST use explicit, versioned contracts. This separation is required so
that hosts, identity systems, GitHub deployments, and local execution mechanisms
can evolve without leaking privileges across layers.

### III. Safe And Recoverable Local Lifecycle

Install, update, rollback, and uninstall operations MUST be allowlisted,
canonical-path constrained, idempotent, concurrency-locked, restart-safe, and
audited. Artifacts MUST be traceable to repository, commit or tag, version, and a
verified checksum or signature before activation.

Update MUST stage and validate the new version before the activation window,
preserve the current version as a controlled backup, use atomic switching where
the host permits it, perform post-install validation, and automatically restore
the prior version on failure. Tests MUST cover interruption and adversarial path,
archive, symlink, and command inputs. Recovery is part of the feature, not a later
operational enhancement.

### IV. Versioned Contracts And Data Privacy

All external input MUST be validated at its boundary, including Skill metadata,
GitHub payloads, API requests, artifact manifests, and local-client messages.
Metadata schema, API contracts, installer protocol, events, and host capabilities
MUST be versioned and compatibility-tested.

Registry data, logs, analytics, errors, and telemetry MUST NOT contain private
source code, prompt contents, access tokens, credentials, or unrestricted local
paths. Rendered metadata MUST be sanitized. Repository metadata version,
published version, latest version, installed version, target version, and
rollback version MUST remain distinct domain concepts.

### V. Incremental Delivery With Proportionate Verification

Implement the smallest independently valuable user journey that satisfies the
approved scope. Non-trivial and user-visible work MUST begin with one complete
SDD slice and measurable acceptance criteria. Architecture and security
decisions MUST be recorded before their implementation makes them expensive to
reverse.

Behavior changes require automated tests at the lowest useful level and
integration or contract tests at affected boundaries. Critical lifecycle journeys
require E2E tests when the participating runtimes exist. Documentation-only and
format-only changes may use proportionate checks. No contributor may claim a
verification result that was not actually run.

## Product And Engineering Constraints

- MVP prioritizes Discover, Detail, Register, metadata validation, version
  management, Install, My Skills, one-click Update with rollback, Uninstall,
  feedback, Owner/Admin basics, SSO/RBAC, themes, and auditability.
- Day 2 features remain isolated and must not delay MVP lifecycle closure.
- Phase, category, tag, host capability, and metadata schema are configurable or
  versioned; no UI or service may silently hard-code them as permanent policy.
- Performance work uses the PRD budgets: initial interaction within 3 seconds on
  the internal network, search P95 within 500 ms, and Skill Detail P95 within
  1 second excluding GitHub calls.
- User-facing experiences target WCAG 2.1 AA and include loading, success, error,
  empty, keyboard, focus, and screen-reader behavior.
- Frameworks, persistence, messaging, deployment, SSO details, local-client
  mechanism, artifact source, and Host capability remain unresolved until approved
  by ADR or research. Git Tag is the product's Published Version authority and the
  pilot retains only the immediately previous local installation; technical plans
  MUST preserve those decisions rather than reopen them implicitly.
- Product documentation may be Chinese. Code, identifiers, schemas, protocol
  fields, ADR titles, and normative technical rules use English.

## Development Workflow

1. Classify the change as trivial or non-trivial using `PROJECT_RULES.md`.
2. For non-trivial work, create or update one slice across the active SDD
   document chain in `docs/01-requirements/` through `docs/06-tasks/`.
3. Clarify scope and unresolved product choices. Record cross-cutting decisions
   in `docs/architecture/decisions/` before implementation.
4. Plan concrete runtime paths, trust boundaries, contracts, failure modes,
   privacy behavior, accessibility, performance impact, and verification.
5. Implement in independently testable slices. Prefer immutable state changes,
   explicit state machines, validated inputs, and replaceable adapters.
6. Run and report the exact relevant lint, typecheck, unit, integration,
   contract, E2E, security, accessibility, and build checks that exist.
7. Update affected requirements, stories, specifications, architecture, design,
   tasks, contracts, ADRs, standards, and user-facing docs in the same change;
   show the diff before commit.

## Governance

This constitution governs the active SDD profile, artifacts, implementation
plans, and reviews. `PROJECT_RULES.md` operationalizes it but cannot weaken it.
An amendment requires documented rationale, a migration or compatibility note
when existing work is affected, and updates to dependent templates and guidance
in the same change.

Version amendments follow semantic versioning: MAJOR for removal or incompatible
redefinition of a principle, MINOR for a new principle or materially expanded
governance, and PATCH for non-semantic clarification. Reviews MUST check the
Constitution Check in each plan before implementation and again after design.
Complexity or exceptions MUST be explicit, scoped, justified against a simpler
alternative, and approved in the feature plan or an ADR.

**Version**: 1.1.0 | **Ratified**: 2026-08-14 | **Last Amended**: 2026-08-18
