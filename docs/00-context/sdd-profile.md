# SDD Profile: atlas-marketplace-sdd

## Status

Accepted

## Applies To

This profile applies to all non-trivial product, architecture, security,
contract, documentation, and cross-runtime changes in Atlas Marketplace.

## Source Of Truth

For an active slice, use this precedence:

1. The accepted specification under `docs/03-spec/` for behavior and scope
2. The constitution, approved product decisions, and PRD for product boundaries
3. Accepted ADRs for cross-cutting technical decisions
4. Architecture, design, contracts, and standards for implementation constraints
5. Prototype and exploratory material as non-authoritative evidence

Requirements and user stories are upstream inputs to the specification. They
must remain traceable, but they do not silently override an accepted downstream
specification. Resolve conflicts by updating and re-reviewing the chain.

## Document Chain

| Order | Stage | Required | Default Path | Primary Skill |
|---|---|---|---|---|
| 0 | Bootstrap and routing | Yes for a full slice | `docs/SDD-BOOTSTRAP.md` | Repository instructions; no orchestration skill |
| 1 | Context and profile | Yes | `docs/00-context/`, `docs/context/`, `docs/domain/`, `docs/architecture/`, relevant `docs/standards/` | Repository instructions and ADRs; no profile skill |
| 2 | Requirements | Yes | `docs/01-requirements/{slice}-requirements.md` | Upstream input; no generation skill in the authoritative set |
| 3 | User stories | Yes | `docs/02-user-stories/{slice}-user-stories.md` | `req-to-user-story` |
| 4 | Specification | Yes | `docs/03-spec/{slice}-spec.md` | `user-story-to-spec` |
| 5 | Architecture | Yes before implementation | `docs/04-architecture/{slice}-architecture.md` | `spec-to-architecture` |
| 6 | Data flow | Required for stateful workflows or integrations | `docs/04-architecture/{slice}-data-flow.md` | `architecture-to-design` |
| 7 | Data model | Required when persistence or domain entities change | `docs/04-architecture/{slice}-data-model.md` | `architecture-to-design` |
| 8 | Detailed design | Yes before implementation | `docs/05-design/{slice}-design.md` | `architecture-to-design` |
| 9 | API/contract guide | Required when boundaries or APIs change | `docs/05-design/contracts/{slice}-API_IMPLEMENTATION_GUIDE.md` | `architecture-to-design` |
| 10 | Tasks | Yes before implementation | `docs/06-tasks/{slice}-tasks.md` | `design-to-tasks` |
| 11 | Traceability and review | Yes | `docs/00-context/{slice}-traceability.md`, `docs/reviews/` | `review-doc-quality` |
| 12 | Implementation | Yes for code changes | Repository source tree | `tasks-to-code` or `tasks-to-implementation` |
| 13 | Code/architecture review | Yes before completion | Review report, diff, and verification evidence | `review-code-against-design`; `architecture-review` when applicable |

## Gates

- Non-trivial or user-facing work updates the SDD chain before implementation.
- Requirements, stories, specification, architecture, design, tasks, and
  traceability must agree on scope before coding.
- Architecture, security-boundary, data-ownership, protocol, stack, or
  cross-tool workflow decisions require an ADR before implementation.
- Existing-code claims must be repository-grounded or tagged `[UNVERIFIED]`,
  `[ASSUMPTION]`, or `[USER-STATED]`.
- A full SDD pass reports its exact skill chain and passes
  `docs/00-context/checklists/sdd-generation-gate.md`.
- Slice-prefixed paths in this profile override generic default filenames in
  mirrored skills. Never create a second unprefixed artifact for the same stage.
- Where generation skills overlap, preserve the accepted upstream artifact and
  update the same slice-prefixed file rather than creating competing outputs.
- Re-read and compare the active upstream artifacts before implementation; do
  not refer to a freshness skill that is absent from the authoritative source.

## Traceability

- Requirements use stable `REQ-*` IDs.
- User stories use stable `US-*` IDs and trace to requirements.
- Specifications preserve detailed functional, security, contract, and success
  IDs and trace to stories.
- Architecture and design trace to specifications and ADRs.
- Tasks trace to design sections and exact verification commands.
- Code, tests, contracts, release notes, and user documentation trace to tasks.

## Language

Project rules and SDD artifacts are English-only. Product documentation may be
Chinese when appropriate. Do not create bilingual SDD duplicates unless the
user explicitly requests them.

## Tool Routing

- `.agents/skills/` is the only project-local skill source.
- The SDD skill family is the only project development workflow.
- `.agents/skills/` must remain an exact mirror of
  `Agentic-SDLC-Control-Tower/.claude/skills/`; Atlas-specific rules belong in
  project documentation, not modified skill copies.
- Validate the mirror with `./scripts/verify-sdd-skills.sh` whenever the skill
  source, lock, or local mirror changes.
- Tool-specific routers must point to the canonical skills instead of copying
  workflow bodies.

## Migration Record

Atlas retired GitHub Spec Kit on 2026-08-18. The previous `.specify/` and
`specs/` workflow was replaced by this profile and the `docs/01`–`docs/06`
chain. Existing product, security, and governance content was preserved in the
new locations. ADR-0002 records the decision and supersedes ADR-0001.

On 2026-08-18, the project replaced its quarry-derived extended skill set with
the authoritative Control Tower skill directory. ADR-0003 records the source,
pinned comparison commit, removals, and exact-mirror policy.

## Related Documents

- `AGENTS.md`
- `PROJECT_RULES.md`
- `docs/00-context/constitution.md`
- `docs/SDD-BOOTSTRAP.md`
- `docs/00-context/checklists/sdd-generation-gate.md`
- `docs/architecture/decisions/ADR-0003-use-control-tower-sdd-skills.md`
