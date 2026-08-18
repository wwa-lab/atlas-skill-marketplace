# Reference Repository Adaptation

The initial document structure was informed by `quarry-kb`, but Atlas does not
copy its product or technology decisions. SDD skills now come exclusively from
`wwa-lab/Agentic-SDLC-Control-Tower/.claude/skills`.

## Adopted Patterns

- thin `AGENTS.md` entry point and concise `PROJECT_RULES.md`
- durable context separated from feature specs
- ADRs for architectural rationale
- runtime-specific implementation standards
- the `docs/00` through `docs/06` SDD document chain
- explicit adapter boundaries for external systems
- strict separation of secrets/runtime data from Git
- exact verification commands only after they exist

## Deliberately Not Copied

- Quarry's FastAPI, Vue, PostgreSQL/pgvector, RAG, and upload assumptions
- Quarry's Admin/Editor/Viewer role vocabulary
- a frontend/backend-only topology; Atlas has a third local execution boundary
- duplicated `.agents/skills` and `.claude/skills` trees
- a blanket handoff manifest for every non-trivial change
- placeholder build commands, local `.env`, virtual environments, caches,
  dependency directories, or generated output

Atlas uses one standalone SDD chain. Its `.agents/skills/` tree is an exact
Codex-facing mirror of Control Tower's `.claude/skills/`; Atlas-specific product
boundaries, trust model, runtime topology, terminology, and technology decisions
remain in project documentation rather than forked skill bodies.
