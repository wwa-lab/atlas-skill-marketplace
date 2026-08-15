# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]

**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

**Note**: This template is filled in by the `/speckit-plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

[Extract from feature spec: primary requirement + technical approach from research]

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: [e.g., Python 3.11, Swift 5.9, Rust 1.75 or NEEDS CLARIFICATION]

**Primary Dependencies**: [e.g., FastAPI, UIKit, LLVM or NEEDS CLARIFICATION]

**Storage**: [if applicable, e.g., PostgreSQL, CoreData, files or N/A]

**Testing**: [e.g., pytest, XCTest, cargo test or NEEDS CLARIFICATION]

**Target Platform**: [e.g., Linux server, iOS 15+, WASM or NEEDS CLARIFICATION]

**Project Type**: [e.g., library/cli/web-service/mobile-app/compiler/desktop-app or NEEDS CLARIFICATION]

**Performance Goals**: [domain-specific, e.g., 1000 req/s, 10k lines/sec, 60 fps or NEEDS CLARIFICATION]

**Constraints**: [domain-specific, e.g., <200ms p95, <100MB memory, offline-capable or NEEDS CLARIFICATION]

**Scale/Scope**: [domain-specific, e.g., 10k users, 1M LOC, 50 screens or NEEDS CLARIFICATION]

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Scope**: Change stays within centralized Registry/lifecycle management and
  does not introduce source hosting, online execution, workflow orchestration,
  public commerce, or unapproved Day 2 scope.
- **Source of truth**: Requirements are traceable to the approved feature spec
  and PRD/product decisions; prototype-only behavior is identified as such.
- **Trust boundaries**: Web, Registry API, GitHub/artifact sources, local
  installer, and host roots remain explicit; no browser/API arbitrary local I/O.
- **Lifecycle safety**: Any install/update/rollback/uninstall work covers
  allowlisted canonical paths, integrity/provenance, staging, backup, locking,
  idempotency, interruption, post-validation, recovery, and audit.
- **Contracts/privacy**: External inputs and cross-runtime payloads use versioned
  schemas; private source, prompts, secrets, and unrestricted paths stay out of
  logs, analytics, registry data, and errors.
- **Verification**: Plan includes proportionate unit, integration, contract, E2E,
  security, accessibility, performance, and failure-path checks.
- **Decisions**: Cross-cutting unresolved choices are marked for research/ADR,
  not silently converted into framework or architecture assumptions.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
apps/marketplace-web/       # Browser UI, if affected
services/registry-api/      # Registry/orchestration, if affected
clients/atlas-installer/    # Trusted local lifecycle client, if affected
packages/contracts/         # Versioned cross-boundary schemas, if affected
packages/host-adapters/     # Host capability adapters, if affected
tests/contract/             # Executable compatibility checks
tests/e2e/                  # Cross-runtime critical journeys
```

**Structure Decision**: [List only affected real directories. Describe internal
feature/module paths selected by stack ADRs and explain any new top-level path.]

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |
