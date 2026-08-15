# Atlas Marketplace

A centralized internal marketplace for discovering and managing AI Skills that
remain owned in distributed GitHub repositories.

The repository currently contains the product source, project rules, Spec Kit
workflow, architecture boundaries, and an implementation-ready directory map.
Application frameworks have intentionally not been selected or scaffolded yet.

## Product Loop

```text
Discover -> Evaluate -> Install -> Update/Rollback -> Uninstall -> Feedback
Register -> Validate -> Preview -> Publish -> Sync
```

Atlas Marketplace manages registry metadata and lifecycle coordination. It is
not a source-code host, online Skill runtime, workflow engine, or public store.

## Start Here

| Document | Purpose |
|---|---|
| `AGENTS.md` | Agent entry point, reading order, boundaries, verification |
| `PROJECT_RULES.md` | Enforceable product, security, coding, and workflow rules |
| `.specify/memory/constitution.md` | Governing principles for Spec Kit changes |
| `docs/product/Atlas_Marketplace_PRD_v0.3.md` | Current product requirements and RPGLE pilot source |
| `specs/001-rpgle-skill-pilot/spec.md` | First end-to-end pilot Feature Specification |
| `docs/context/product-boundaries.md` | MVP, Day 2, and non-goal boundary |
| `docs/context/open-questions.md` | Decisions that must not be assumed |
| `docs/domain/README.md` | Initial entities, relationships, and state boundaries |
| `docs/architecture/system-overview.md` | Runtime responsibilities and data flow |
| `docs/architecture/security-boundaries.md` | Trust zones and local-operation safety |

## Repository Layout

```text
.agents/skills/             Spec Kit agent skills
.specify/                   Spec Kit workflow, templates, and constitution
apps/marketplace-web/       Browser UI boundary
services/registry-api/      Registry and orchestration API boundary
clients/atlas-installer/    Trusted local lifecycle client boundary
packages/contracts/         Versioned schemas and protocols
packages/host-adapters/     Host adapters; pilot targets VS Code Copilot Chat
specs/                      One directory per non-trivial feature/change
docs/product/               PRD and prototype sources
docs/context/               Durable terminology, boundaries, open questions
docs/domain/                Cross-feature entities and lifecycle vocabulary
docs/architecture/          System/security design and ADRs
docs/standards/             Runtime-specific implementation standards
tests/contract/             Cross-boundary contract tests
tests/e2e/                  Critical end-to-end journeys
infra/                      Infrastructure after stack/deployment decisions
scripts/                    Repository automation after a concrete need exists
```

Boundary directories contain only a README until a feature plan and ADR select
the relevant technology. This prevents empty scaffolding from becoming an
accidental architecture decision.

## Change Workflow

For a non-trivial change:

1. Read `PROJECT_RULES.md` and the relevant product/context documents.
2. Create one Spec Kit feature under `specs/`.
3. Resolve architecture decisions with an ADR where necessary.
4. Plan, implement, and verify against the feature's acceptance criteria.
5. Keep contracts, tests, and documentation in the same change.

Small wording and formatting fixes that do not alter behavior may be handled
directly.

## Current Verification

No build commands exist before runtime scaffolding. Repository-document checks:

```sh
git diff --check
if rg -n '\[(PROJECT_NAME|PRINCIPLE_[0-9]+_[A-Z_]+|SECTION_[0-9]+_[A-Z_]+|GOVERNANCE_RULES|CONSTITUTION_VERSION|RATIFICATION_DATE|LAST_AMENDED_DATE)\]' .specify/memory/constitution.md; then
  echo "unresolved constitution placeholders found" >&2
  exit 1
fi
```

## Reference Material

The structure borrows the useful separation found in `quarry-kb`: a thin agent
entry point, short hard rules, durable context, ADRs, layer standards, and clear
runtime boundaries. It deliberately does not copy Quarry's FastAPI/Vue/RAG
decisions or its multi-document SDD chain.
