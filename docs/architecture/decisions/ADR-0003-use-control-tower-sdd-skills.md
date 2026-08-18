# ADR-0003: Use Control Tower as the Authoritative SDD Skill Source

## Status

Accepted

## Date

2026-08-18

## Context

ADR-0002 established standalone SDD but populated `.agents/skills/` from a
quarry-derived, locally adapted set. The project owner subsequently identified
`wwa-lab/Agentic-SDLC-Control-Tower/.claude/skills` as the required skill source
and directed Atlas to remove any skill that is not present there.

Comparison against Control Tower `main` at commit
`8bed9dbf13ae4aa4307e3b4344fa3f64873554cd` found:

- six same-name directories with identical content;
- four same-name directories with Atlas-local modifications;
- ten Atlas-only skill directories;
- one Control-Tower-only skill, `architecture-review`.

## Decision

Control Tower's `.claude/skills/` directory is the authoritative SDD skill
source. Atlas mirrors that directory exactly into `.agents/skills/` for Codex.

- Add, remove, and replace complete skill directories as upstream changes.
- Do not customize mirrored `SKILL.md`, reference, or example files in Atlas.
- Keep Atlas-specific paths, gates, and governance in `AGENTS.md`,
  `PROJECT_RULES.md`, and `docs/`, outside the mirrored skill tree.
- Record the upstream comparison commit in the local skill registry and verify
  each synchronization with `diff -qr`.

This ADR supersedes ADR-0002 while retaining its decisions to use standalone
SDD, retire Spec Kit, and keep the `docs/01` through `docs/06` artifact chain.

## Alternatives Considered

| Alternative | Why Not |
|---|---|
| Keep the quarry-derived 19-skill set | It is not the skill family selected by the project owner and contains unsupported local extensions. |
| Keep only matching names but preserve Atlas modifications | Same-name skills would still drift from the authoritative source. |
| Use `.claude/skills/` directly without a Codex mirror | Codex discovers project-local skills from `.agents/skills/`; a verified exact mirror preserves compatibility without forking behavior. |

## Consequences

### Positive

- Atlas has one explicit upstream source for all project-local SDD skills.
- Skill names, bodies, references, and examples are reproducible and auditable.
- Atlas-specific governance can evolve without silently forking upstream skills.

### Negative

- Upstream `architecture-review` contains Control Tower stack assumptions and
  applies only when its trigger conditions match Atlas code.
- Removed orchestration, profile, freshness, ADR, manifest, and doctor skills
  are no longer callable as project-local skills.
- Upstream updates require a deliberate re-sync and registry commit update.

### Neutral / Operational

- SDD stages are routed directly through the available skills; project docs,
  not a custom orchestrator skill, define the full Atlas chain.
- Historical ADRs retain the rationale for the earlier quarry-derived setup.

## Review Triggers

Revisit this decision when:

- Control Tower changes its canonical skill directory or compatibility model;
- Codex can consume the upstream `.claude/skills/` directory directly;
- Atlas needs a project-specific skill that should be contributed upstream.

## Related Documents

- `docs/00-context/agentic-sdlc-registry.md`
- `docs/00-context/sdd-profile.md`
- `docs/SDD-BOOTSTRAP.md`
- `AGENTS.md`
- `PROJECT_RULES.md`
