# ADR-0001: Adopt the Atlas SDD Profile and Project-Local Skills

- **Status**: Superseded by ADR-0002
- **Date**: 2026-08-18
- **Owners**: Atlas Marketplace maintainers
- **Related Spec**: `docs/03-spec/rpgle-skill-pilot-spec.md`

## Context

Atlas already uses GitHub Spec Kit for feature scope, but its rules did not
yet include the grounding, traceability, freshness, handoff, and review skill
chain used by the quarry-kb sample repository. Adopting the sample repository's
full `docs/01`–`docs/06` layout unchanged would conflict with Atlas's existing
rule that `specs/{feature}/` is the single source of change intent.

## Decision Drivers

- Require SDD discipline for non-trivial and user-facing changes.
- Preserve Atlas's existing Spec Kit source-of-truth and product boundaries.
- Keep one canonical local skill library for Codex and future tool routers.
- Prevent ungrounded generated documents and stale handoffs from reaching code.

## Considered Options

1. Keep Spec Kit only and add no SDD skill layer.
2. Copy quarry-kb's `docs/01`–`docs/06` chain and run two parallel workflows.
3. Adopt the SDD rules and skills, mapping their stages onto Atlas Spec Kit
   artifacts.

## Decision

Choose option 3. Copy the reusable SDD skills into `.agents/skills/`, retain
the existing Spec Kit skills, and use `docs/00-context/sdd-profile.md` to map
SDD stages onto `specs/{slice}/spec.md`, `plan.md`, `tasks.md`, and conditional
supporting artifacts. Do not create a parallel requirements/design/task chain.

Non-trivial work must pass the active slice's SDD gates before implementation.
Cross-cutting architecture, security, protocol, data-ownership, and workflow
decisions still require separate ADRs under `docs/architecture/decisions/`.

## Consequences

### Positive

- Generated SDD artifacts are grounded, traceable, and reviewable.
- Existing Atlas Spec Kit work remains the authoritative change surface.
- Future agents can use the same local skill family without relying on chat-only
  process knowledge.

### Negative / Trade-offs

- Non-trivial work has additional document and review gates.
- The copied skills require project-profile routing and must not be used with
  quarry-kb-specific stack assumptions.
- Cross-tool native bridges are not automatically available until added.

## Security And Privacy Impact

The SDD gates reinforce Atlas input validation, trust-boundary separation,
artifact provenance, path safety, privacy, auditability, and recovery rules.
They do not authorize any new filesystem, shell, GitHub, or external action.

## Compatibility And Migration

Existing `specs/` and `.specify/` artifacts remain valid. The current RPGLE pilot
spec is still Draft and must receive a concrete plan, tasks, and applicable
reviews before implementation. No existing product or runtime boundary is
changed by this decision.

## Verification

- Confirm the copied SDD skills exist under `.agents/skills/`.
- Confirm the SDD profile maps to Spec Kit and forbids a parallel chain.
- Run `git diff --check` and the repository constitution placeholder check.
- Validate `docs/00-context/execution-manifest.schema.json` as JSON.
