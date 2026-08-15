# Reference Repository Adaptation

The initial structure was informed by `quarry-kb`, but Atlas is not a copy of its
technology or document process.

## Adopted Patterns

- thin `AGENTS.md` entry point and concise `PROJECT_RULES.md`
- durable context separated from feature specs
- ADRs for architectural rationale
- runtime-specific implementation standards
- explicit adapter boundaries for external systems
- strict separation of secrets/runtime data from Git
- exact verification commands only after they exist

## Deliberately Not Copied

- Quarry's FastAPI, Vue, PostgreSQL/pgvector, RAG, and upload assumptions
- Quarry's Admin/Editor/Viewer role vocabulary
- a frontend/backend-only topology; Atlas has a third local execution boundary
- the parallel `docs/01` through `docs/06` SDD chain
- duplicated `.agents/skills` and `.claude/skills` trees
- a blanket handoff manifest for every non-trivial change
- placeholder build commands, local `.env`, virtual environments, caches,
  dependency directories, or generated output

Atlas uses Spec Kit as its single feature/change planning backbone and adds only
the durable product, architecture, security, and standards documents that should
outlive an individual feature.
