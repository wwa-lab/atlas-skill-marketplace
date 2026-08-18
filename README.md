# Atlas Marketplace

A centralized internal marketplace for discovering and managing AI Skills that
remain owned in distributed GitHub repositories.

The repository currently contains the product source, project rules, standalone
SDD workflow, architecture boundaries, and an implementation-ready directory map.
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
| `docs/00-context/constitution.md` | Governing product and engineering principles |
| `docs/00-context/sdd-profile.md` | Required SDD stages, paths, gates, and skill routing |
| `docs/product/Atlas_Marketplace_PRD_v0.3.md` | Current product requirements and RPGLE pilot source |
| `docs/03-spec/rpgle-skill-pilot-spec.md` | First end-to-end pilot specification |
| `docs/context/product-boundaries.md` | MVP, Day 2, and non-goal boundary |
| `docs/context/open-questions.md` | Decisions that must not be assumed |
| `docs/domain/README.md` | Initial entities, relationships, and state boundaries |
| `docs/architecture/system-overview.md` | Runtime responsibilities and data flow |
| `docs/architecture/security-boundaries.md` | Trust zones and local-operation safety |

## Repository Layout

```text
.agents/skills/             Canonical project-local SDD skills
apps/marketplace-web/       Browser UI boundary
services/registry-api/      Registry and orchestration API boundary
clients/atlas-installer/    Trusted local lifecycle client boundary
packages/contracts/         Versioned schemas and protocols
packages/host-adapters/     Host adapters; pilot targets VS Code Copilot Chat
docs/00-context/            Constitution, SDD profile, traceability, handoff
docs/01-requirements/       Slice requirements
docs/02-user-stories/       User stories and acceptance criteria
docs/03-spec/               Behavior specifications
docs/04-architecture/       Slice architecture, data flow, and data model
docs/05-design/             Detailed design and contracts
docs/06-tasks/              Implementation task breakdowns
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
2. Create or update one slice through `docs/01-requirements/` to
   `docs/06-tasks/`.
3. Resolve architecture decisions with an ADR where necessary.
4. Pass `review-doc-quality` before implementation and run the applicable
   post-implementation reviews.
5. Keep contracts, tests, and documentation in the same change.

Small wording and formatting fixes that do not alter behavior may be handled
directly.

## Current Verification

No build commands exist before runtime scaffolding. Repository-document checks:

```sh
# The mirrored upstream Skills retain upstream formatting and are validated
# separately by verify-sdd-skills.sh.
git diff --check -- . ':(exclude).agents/skills/**'
if rg -n '\[(PROJECT_NAME|PRINCIPLE_[0-9]+_[A-Z_]+|SECTION_[0-9]+_[A-Z_]+|GOVERNANCE_RULES|CONSTITUTION_VERSION|RATIFICATION_DATE|LAST_AMENDED_DATE)\]' docs/00-context/constitution.md; then
  echo "unresolved constitution placeholders found" >&2
  exit 1
fi
```

For SDD skill mirror or lock-file changes, also run:

```sh
./scripts/verify-sdd-skills.sh
```

## Reference Material

The document structure borrows useful separation from `quarry-kb`, without
copying its FastAPI/Vue/RAG decisions. The project-local SDD skill library is an
exact mirror of the authoritative
[`Agentic-SDLC-Control-Tower/.claude/skills`](https://github.com/wwa-lab/Agentic-SDLC-Control-Tower/tree/main/.claude/skills)
directory; Atlas-specific workflow rules remain in project documentation rather
than modified copies of those skills.
