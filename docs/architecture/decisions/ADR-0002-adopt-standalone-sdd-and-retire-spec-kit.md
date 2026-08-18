# ADR-0002: Adopt Standalone SDD and Retire Spec Kit

- **Status**: Superseded by ADR-0003
- **Date**: 2026-08-18
- **Owners**: Atlas Marketplace maintainers
- **Related Spec**: `docs/03-spec/rpgle-skill-pilot-spec.md`

## Context

ADR-0001 added the quarry-kb-derived SDD skills while retaining GitHub Spec Kit
as a second workflow surface. That compatibility approach preserved existing
artifacts but left overlapping entry points, duplicated concepts, and ambiguity
about whether agents should use `specs/` or the standalone SDD stages.

Atlas has not started runtime implementation. The migration cost is therefore
low, and the project owner explicitly chose a single SDD skill and document
workflow.

## Decision Drivers

- Keep one unambiguous development workflow.
- Match the proven quarry-kb requirements-to-tasks SDD chain.
- Preserve Atlas product, security, architecture, and governance content.
- Avoid duplicate feature, planning, task, and review tools.

## Considered Options

1. Keep the ADR-0001 compatibility model with both Spec Kit and SDD skills.
2. Keep Spec Kit and remove the standalone SDD skill family.
3. Retire Spec Kit and use only the standalone SDD skill family and document
   chain.

## Decision

Choose option 3.

- `.agents/skills/` contains only the standalone SDD and Agentic SDLC skill
  family.
- `.specify/`, local `speckit-*` skills, and the `specs/` feature tree are
  retired.
- Atlas uses `docs/01-requirements/` through `docs/06-tasks/` as its only
  feature-development document chain.
- `docs/03-spec/` is the source of accepted behavior and scope for a slice.
- The constitution moves to `docs/00-context/constitution.md`.
- Existing RPGLE pilot content is migrated without inventing unresolved
  architecture, design, or implementation decisions.

This ADR supersedes ADR-0001.

## Consequences

### Positive

- Agents and maintainers have one entry point and one artifact chain.
- Requirements, stories, specification, architecture, design, tasks, code, and
  tests can be traced explicitly.
- The project no longer depends on Spec Kit scripts, templates, or skill names.

### Negative / Trade-offs

- GitHub Spec Kit commands and templates are no longer available locally.
- The SDD chain has more artifacts and review gates than the former compact
  spec/plan/tasks workflow.
- Existing feature artifacts require migration and link updates.

## Security And Privacy Impact

No runtime trust boundary or permission changes. The standalone SDD gates retain
and strengthen explicit security, privacy, artifact provenance, path safety,
recovery, and audit requirements.

## Compatibility And Migration

- Preserve the constitution under `docs/00-context/constitution.md` and bump it
  to version 1.1.0.
- Move the RPGLE specification to `docs/03-spec/` and create upstream
  requirements, user stories, and traceability artifacts.
- Leave architecture, design, contracts, and tasks explicitly incomplete until
  the blocking technical decisions are resolved.
- Update all repository rules, docs, skills, and test guidance to the new paths.

## Verification

- No `.specify/`, `specs/`, or local `speckit-*` skill remains.
- No live repository document or skill references Spec Kit paths.
- Required SDD skills and `docs/01`–`docs/06` directories exist.
- `git diff --check`, constitution placeholder checks, JSON validation, and SDD
  structural checks pass.
