# RPGLE Skill Pilot Traceability

## Status

Current as of 2026-08-18. The design chain is complete; real integration and
pilot-release work remains blocked by the decisions mapped below.

## Artifact Chain

| Stage | Artifact | Status |
|---|---|---|
| Product source | `docs/product/Atlas_Marketplace_PRD_v0.3.md` | Draft product source |
| Requirements | `docs/01-requirements/rpgle-skill-pilot-requirements.md` | Accepted for architecture |
| User stories | `docs/02-user-stories/rpgle-skill-pilot-user-stories.md` | Accepted for specification |
| Specification | `docs/03-spec/rpgle-skill-pilot-spec.md` | Accepted for architecture |
| Architecture | `docs/04-architecture/rpgle-skill-pilot-architecture.md` | Accepted for design |
| Data flow | `docs/04-architecture/rpgle-skill-pilot-data-flow.md` | Accepted for design |
| Data model | `docs/04-architecture/rpgle-skill-pilot-data-model.md` | Accepted for tasks |
| Design | `docs/05-design/rpgle-skill-pilot-design.md` | Accepted for tasks with blockers |
| Contracts | `docs/05-design/contracts/rpgle-skill-pilot-API_IMPLEMENTATION_GUIDE.md` | Accepted for tasks with blocked integrations |
| Tasks | `docs/06-tasks/rpgle-skill-pilot-tasks.md` | Accepted backlog; blocked tasks explicit |
| Review | `docs/reviews/rpgle-skill-pilot-sdd-review.md` | Not ready for full pilot; ready for unblocked local slices |

## Requirement-To-Story Mapping

| Requirement Group | Stories | Detailed Spec IDs |
|---|---|---|
| REQ-RPGLE-001 | US-RPGLE-001 | FR-001–FR-008 |
| REQ-RPGLE-002 | US-RPGLE-002 | FR-009–FR-015 |
| REQ-RPGLE-003 | US-RPGLE-003 | FR-016–FR-022 |
| REQ-RPGLE-004 | US-RPGLE-004 | FR-031–FR-034 |
| REQ-RPGLE-005 | US-RPGLE-005, US-RPGLE-006 | FR-023–FR-030, FR-035 |
| REQ-RPGLE-006 | US-RPGLE-001, US-RPGLE-005, US-RPGLE-006 | FR-036–FR-040 |
| REQ-RPGLE-007 | US-RPGLE-007 | FR-041 |
| REQ-RPGLE-008 | US-RPGLE-002–US-RPGLE-007 | SPR-001–SPR-006 |
| REQ-RPGLE-009 | US-RPGLE-002, US-RPGLE-003, US-RPGLE-005 | CCR-001–CCR-005 |
| REQ-RPGLE-010 | US-RPGLE-001–US-RPGLE-007 | SC-001–SC-007 |

## Implementation Gate

TASK-001 through TASK-016 may proceed using only local/test fake adapters where
their listed dependencies are satisfied. TASK-017 through TASK-022 resolve or
implement currently blocked real boundaries. Full pilot implementation and
release acceptance remain blocked until TASK-017 and TASK-019–TASK-022 complete.

## Specification-To-Design-To-Task Mapping

| Spec IDs | Architecture / Design ownership | Tasks |
|---|---|---|
| FR-001–FR-008 | Catalog, Access, Discover/Detail UI | TASK-007, TASK-008 |
| FR-009–FR-015 | Lifecycle orchestration, Installer port, operation UI | TASK-009, TASK-010, TASK-019, TASK-020 |
| FR-016–FR-022, FR-041 | Installation/Backup/Job states and lifecycle UI | TASK-011, TASK-012, TASK-019, TASK-020 |
| FR-023–FR-030, FR-035 | Publication, Sync, Ownership, GitHub adapter | TASK-013, TASK-014, TASK-017, TASK-018, TASK-021 |
| FR-031–FR-034 | Feedback and Certification | TASK-015, TASK-016, TASK-022 |
| FR-036–FR-040 | Identity/Access, safe UI projections | TASK-004, TASK-007, TASK-008, TASK-017, TASK-018 |
| SPR-001–SPR-006 | Security, audit, redaction, lifecycle safety | TASK-002, TASK-004, TASK-005, TASK-009, TASK-020, TASK-021, TASK-023 |
| CCR-001–CCR-005 | Contracts and adapters | TASK-002, TASK-019–TASK-021, TASK-023 |
| SC-001–SC-007 | Release-quality verification | TASK-023 |

## Decision Mapping

| Decision | Status | Tasks affected |
|---|---|---|
| ADR-0004 Web/API/database stack | Accepted | TASK-001–TASK-006, TASK-023–TASK-024 |
| OQ-002/OQ-009 identity and restricted indexing | Open | TASK-017–TASK-018 |
| OQ-003/OQ-004/OQ-005/OQ-010 local lifecycle | Open | TASK-019–TASK-020 |
| OQ-006 artifact supply chain | Open | TASK-021 |
| OQ-007/OQ-008 evidence and independence | Open | TASK-022 |
