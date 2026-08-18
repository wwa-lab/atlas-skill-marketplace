# SDD Bootstrap: Atlas Marketplace

## Purpose

Use this guide when a new or materially updated Atlas slice needs an
implementation-ready SDD set. Atlas uses one standalone SDD document chain and
one canonical project-local skill library.

## Skill Source

Use only the skills listed in
`docs/00-context/agentic-sdlc-registry.md`. The local `.agents/skills/` tree is
an exact mirror of the authoritative Control Tower `.claude/skills/` directory;
there is no separate full-chain orchestration skill.

## Atlas Path Precedence

The slice-prefixed paths in this document and
`docs/00-context/sdd-profile.md` override generic output filenames shown inside
mirrored skills. Always write to `{slice}-*` paths in the Slice Document Set
below. Do not create parallel unprefixed files such as
`docs/04-architecture/architecture.md`, `docs/05-design/design.md`, or
`docs/06-tasks/tasks.md`.

## Mandatory Skill Chain

| Order | Skill | Purpose |
|---|---|---|
| 1 | `req-to-user-story` | Requirements → user stories and acceptance criteria |
| 2 | `user-story-to-spec` | User stories → implementation-facing specification |
| 3 | `spec-to-architecture` | Specification → architecture |
| 4 | `architecture-to-design` | Specification/architecture → architecture, data flow, data model, design, and contracts |
| 5 | `design-to-tasks` | Design → ordered implementation tasks |
| 6 | `review-doc-quality` | Completeness, consistency, grounding, and readiness review |
| 7 | `tasks-to-code` or `tasks-to-implementation` | Implement the accepted task set according to the selected skill's trigger |
| 8 | `review-code-against-design` | Review implementation against the accepted design |
| 9 | `architecture-review` | Review extensibility and decoupling when its stack-specific trigger applies |

Create an ADR under `docs/architecture/decisions/` whenever project rules
require one. ADR handling is a repository rule, not a locally invented skill.
Use implementation and code-review skills only after the document gate passes.

## Overlap And Selection Rules

| Situation | Use | Routing rule |
|---|---|---|
| Establish the architecture stage from an accepted spec | `spec-to-architecture` | Create or update the slice-prefixed architecture and data-flow artifacts, then review them before design work |
| Produce detailed design artifacts from an accepted architecture | `architecture-to-design` | Preserve the accepted architecture decision; update the same slice-prefixed architecture/data-flow files only when necessary, and create data model, design, and contracts without parallel files |
| Implement focused incremental tasks in an established codebase | `tasks-to-code` | Use for a bounded, reviewable task subset that extends existing runtime conventions |
| Bootstrap a new runtime, perform a broad implementation pass, or execute a migration | `tasks-to-implementation` | Select and report Greenfield, Brownfield, or Migration mode as required by the skill |
| Review a document before handing it downstream | `review-doc-quality` | Run at each material transition: stories → spec, spec → architecture, architecture → design, and design → tasks |

Do not run both implementation skills for the same task set. For Atlas's
currently unscaffolded runtimes, use `tasks-to-implementation` until an
established implementation and test structure exists.

## Required Context

Read, in this order:

1. `AGENTS.md`
2. `PROJECT_RULES.md`
3. `docs/00-context/constitution.md`
4. `docs/00-context/sdd-profile.md`
5. Relevant slice documents under `docs/01-requirements/` through
   `docs/06-tasks/`
6. `docs/context/product-boundaries.md` and `docs/context/glossary.md`
7. `docs/domain/README.md` when entities or lifecycle states change
8. Relevant `docs/standards/` files
9. Relevant ADRs under `docs/architecture/decisions/`
10. `docs/product/Atlas_Marketplace_PRD_v0.3.md` when product scope or
    acceptance criteria are affected

## Slice Document Set

Generate or update:

1. `docs/01-requirements/{slice}-requirements.md`
2. `docs/02-user-stories/{slice}-user-stories.md`
3. `docs/03-spec/{slice}-spec.md`
4. `docs/04-architecture/{slice}-architecture.md`
5. `docs/04-architecture/{slice}-data-flow.md` when stateful workflows or
   integrations exist
6. `docs/04-architecture/{slice}-data-model.md` when persistence or entities
   change
7. `docs/05-design/{slice}-design.md`
8. `docs/05-design/contracts/{slice}-API_IMPLEMENTATION_GUIDE.md` when APIs or
   cross-boundary contracts change
9. `docs/06-tasks/{slice}-tasks.md`
10. `docs/00-context/{slice}-traceability.md`
11. A quality review under `docs/reviews/`

## Quality Gate

Before implementation handoff, verify:

- [ ] Goal, scope, exclusions, actors, acceptance, verification, and constraints are explicit.
- [ ] Requirements, stories, specification, architecture, design, and tasks agree.
- [ ] Trust boundaries, authorization, privacy, audit, and failure recovery are explicit.
- [ ] External inputs and cross-boundary contracts are versioned and validated.
- [ ] Architecture-impacting decisions have an ADR or are explicit blockers.
- [ ] Existing-code claims are grounded or tagged.
- [ ] All output paths follow Atlas slice-prefixed naming; no parallel generic artifact exists.
- [ ] Overlapping generation or implementation skills have one explicit owner for each output/task set.
- [ ] `review-doc-quality` passed.
- [ ] `./scripts/verify-sdd-skills.sh` passed for changes that add, remove, or update mirrored skills.
- [ ] The completion report lists the exact skill chain used.

## Final Handoff Format

Summarize the slice, files, scope/non-goals, ADR status, open questions,
skill-chain evidence, review results, and exact implementation handoff.
