# Domain Model

This document captures the stable, cross-feature domain shape confirmed in PRD
v0.3. Feature specs and data-model plans refine it; persistence schemas must not
silently redefine it.

## Core Relationships

```text
Identity/User ---- Membership/Role ---- Owner/Team
     |                                  |
     |                                  +---- Skill ---- SkillVersion ---- Certification
     |                                          |             |
     +---- Installation ---- UpdateJob          |             +---- DistributionArtifact
     |            |              |              |
     |            +---- Backup/RollbackRecord   +---- RepositorySync
     |
     +---- Feedback ---------------------------- Skill

Admin configuration ---- SDLCPhase / Category / Tag / FeaturedPlacement
AuditEvent ------------ actor + action + target + outcome
```

## MVP Aggregates

- **Skill**: registry identity, ownership, repository pointer, classification,
  visibility, verification, presentation, and lifecycle.
- **SkillVersion**: immutable publishable version, changelog, compatibility,
  instructions, artifact references, and publication lifecycle.
- **Installation**: user + Skill + host/environment, active installed version,
  state, canonical local identity, and latest reconciled result.
- **UpdateJob**: durable lifecycle state machine for install/update/rollback or
  uninstall; owns idempotency and progress but not arbitrary executable commands.
- **Backup/RollbackRecord**: the one restorable immediately previous version,
  policy timestamps, verified local identity, and restoration outcome.
- **DistributionArtifact**: immutable artifact provenance, integrity, format,
  compatibility, and availability.
- **RepositorySync**: requested/observed repository revision, metadata validation,
  result, and publication eligibility.
- **Feedback**: user, Skill/version/host context, rating/usefulness/category,
  comment, lifecycle, and Owner association.
- **Certification**: one SME assessment bound to one Skill Version and standard
  RPGLE test-set identity. A new version does not inherit it.
- **AuditEvent**: append-only evidence for security- and lifecycle-relevant
  actions without private source, prompt, secret, or unrestricted path content.

Identity, Owner/Team, Primary Maintainer, confirmation, taxonomy, visibility, and
version-specific certification are pilot concerns.

## Day 2 Entities

Collection, Favorite, personalized Recommendation, QualitySnapshot,
AdoptionMetric, and advanced Alert remain outside the pilot unless a feature spec
and product decision explicitly promote them.

## State And Version Rules

- State transitions are explicit and validated; do not infer lifecycle from
  missing timestamps or strings displayed by the UI.
- Stored state vocabularies must follow PRD v0.3 and the active feature spec;
  unresolved technical contracts remain in `docs/context/open-questions.md`.
- Repository metadata, published, latest eligible, installed, target, and
  previous/rollback versions are distinct typed concepts.
- An artifact references exactly one source revision/version identity. A mutable
  URL alone is not sufficient provenance.
- Registry job completion is not evidence of local success until the local client
  reports a verified terminal result; reconciliation handles interrupted reports.
