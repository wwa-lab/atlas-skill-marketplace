# Requirements: RPGLE Skill Marketplace Pilot

## Status

Draft — migrated from the original Atlas feature specification on 2026-08-18.

## Context

Atlas needs one end-to-end product proof showing that an employee can discover,
evaluate, install, invoke, maintain, and safely remove an AI Skill whose source
remains in an owner-controlled GitHub repository. The pilot uses one IBM iSeries
RPGLE program-analysis Skill on Windows with VS Code GitHub Copilot Chat.

## Goals

- Prove the full consumer lifecycle from discovery through safe uninstall.
- Prove an owner can publish and maintain versioned Skill releases.
- Prove that local lifecycle operations remain constrained, recoverable, and
  auditable.
- Produce measurable evidence from ten first-time target users without creating
  a separate entitlement path for that cohort.

## In Scope

- Discovery, detail, certification evidence, and access-aware presentation.
- Personal and Project installation scopes.
- Explicit Host invocation and validation of useful RPGLE output.
- User-confirmed updates, automatic technical rollback, manual quality rollback,
  and safe uninstall.
- GitHub registration, owner confirmation, tagged publication, and repository
  synchronization.
- Version-specific feedback, certification, audit, SSO/RBAC, and privacy rules.

## Out Of Scope

- macOS or Linux support.
- Hosts other than VS Code GitHub Copilot Chat.
- Semantic search, online Skill execution, Agent Runtime, workflow orchestration,
  Prompt Playground, source editing, or source/prompt upload.
- Silent background updates or more than one retained local backup.
- Day 2 Leaderboard, Trending, Collections, Favorites, recommendations, mature
  quality scoring, or Owner analytics.
- Public marketplace, payments, subscriptions, or settlement.

## Requirement Groups

The downstream specification preserves the detailed normative IDs (`FR-*`,
`SPR-*`, `CCR-*`, and `SC-*`). This document groups those requirements for
upstream traceability without duplicating their full normative text.

| ID | Requirement | Source | Priority | Detailed Spec IDs |
|---|---|---|---|---|
| REQ-RPGLE-001 | Employees can discover and evaluate the RPGLE Skill using canonical IBM iSeries taxonomy, aliases, truthful ranking, curated evidence, and version certification details. | PRD v0.3; product boundaries | Must | FR-001–FR-008 |
| REQ-RPGLE-002 | Eligible Windows users can install to Personal or Project scope and explicitly invoke the Skill in VS Code GitHub Copilot Chat to receive a valid RPGLE analysis result. | PRD v0.3 | Must | FR-009–FR-015 |
| REQ-RPGLE-003 | Users can review, confirm, apply, recover from, and manually roll back updates independently per installation scope. | PRD v0.3; constitution | Must | FR-016–FR-022 |
| REQ-RPGLE-004 | Consumers can report version-specific missed dependencies and track owner responses without submitting source code or prompts. | PRD v0.3 | Must | FR-031–FR-034 |
| REQ-RPGLE-005 | Authorized maintainers can register, validate, publish, synchronize, certify, and transfer ownership of the Skill with explicit accountability. | PRD v0.3; GitHub boundary | Must | FR-023–FR-030, FR-035 |
| REQ-RPGLE-006 | Catalog visibility, artifact access, installation eligibility, ownership, and governance respect repository and company authorization boundaries. | Product boundaries; security boundaries | Must | FR-036–FR-040 |
| REQ-RPGLE-007 | Users can uninstall one managed scope without altering another scope or unrelated files, including interrupted and retried removal. | PRD v0.3; installer safety rules | Must | FR-041 |
| REQ-RPGLE-008 | All external inputs, rendered metadata, artifacts, paths, lifecycle operations, logs, analytics, and audit records satisfy Atlas security and privacy constraints. | Constitution; security boundaries | Must | SPR-001–SPR-006 |
| REQ-RPGLE-009 | Metadata, artifact, local lifecycle, Host capability, consent, provenance, and rollback boundaries use versioned compatibility contracts. | Constitution; open questions | Must | CCR-001–CCR-005 |
| REQ-RPGLE-010 | The pilot produces measurable discovery, invocation, recovery, authorization, certification, feedback, and path-safety evidence. | PRD v0.3 | Must | SC-001–SC-007 |

## Constraints

- Marketplace remains a registry and lifecycle manager, not a source-code host
  or online runtime.
- Git Tag is the Published Version authority.
- Project scope takes precedence over Personal scope within that Project.
- Only the immediately previous verified local version is retained.
- Browser and Registry API never perform arbitrary local shell or filesystem
  operations.
- Repository source, prompts, secrets, and unrestricted local paths never enter
  registry data, analytics, feedback, logs, errors, or telemetry.

## Open Questions

The technical decisions in `docs/context/open-questions.md` remain unresolved.
They block architecture, design, and implementation, but do not reopen the user
outcomes and product constraints above.

## Source Documents

- `docs/product/Atlas_Marketplace_PRD_v0.3.md`
- `docs/context/product-boundaries.md`
- `docs/context/open-questions.md`
- `docs/architecture/security-boundaries.md`
- `docs/00-context/constitution.md`
