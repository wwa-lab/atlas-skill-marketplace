# SDD Skills Playbook

## Goal

Keep one portable SDD workflow library available to Atlas coding agents.

## Canonical Source

The authoritative upstream source is:

`https://github.com/wwa-lab/Agentic-SDLC-Control-Tower/tree/main/.claude/skills`

The project-local exact mirror is:

`.agents/skills/`

Use the skill-specific `SKILL.md` as the workflow source. Do not customize the
mirrored skill bodies; Atlas-specific rules belong in project documentation.

## Routing

- Codex reads `.agents/skills/` and the active profile in
  `docs/00-context/sdd-profile.md`.
- SDD skills create and update the document chain under `docs/01`–`docs/06`.
- Other coding tools should route to the same local skills when they support
  repository skill loading.
- Native tool bridges may be added later, but they must remain thin pointers to
  the canonical local source.

## Core Chain

For a complete slice, route directly through the applicable authoritative
skills:

`req-to-user-story` → `user-story-to-spec` → `spec-to-architecture` →
`architecture-to-design` → `design-to-tasks` → `review-doc-quality` →
`tasks-to-code` or `tasks-to-implementation` →
`review-code-against-design` → `architecture-review` when applicable.

There is no project-local orchestration skill. The SDD profile and bootstrap
document coordinate the chain without duplicating skill behavior.

## Grounding Discipline

Every generated SDD artifact must follow the shared grounding rules:

- verify claims about existing code;
- tag unverified or assumed claims;
- trace new rules against edge cases;
- run a contradiction and phase-scope sweep;
- commit implementation-impacting decisions or surface them as open questions;
- re-check upstream claims instead of inheriting them blindly.
