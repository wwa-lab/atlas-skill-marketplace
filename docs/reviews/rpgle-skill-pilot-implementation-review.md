# RPGLE Skill Pilot Implementation Review

## Review Scope

- **Design reviewed:** `docs/05-design/rpgle-skill-pilot-design.md` and API guide
- **Tasks reviewed:** `docs/06-tasks/rpgle-skill-pilot-tasks.md`
- **Code inspected:** registry API, Vue application, migrations, contracts, tests, CI
- **Review objective:** assess this greenfield implementation slice against the accepted SDD chain

## Overall Assessment

- **Alignment rating:** 64%
- **Verdict:** Partially aligned; suitable for local pilot testing only
- **Rationale:** The catalog and local fake-backed request flows preserve the runtime
  boundaries, validation, persistence, error, idempotency, and audit intent. The real
  SSO, GitHub, installer, artifact, certification, reconciliation, and moderation
  flows remain deliberately unimplemented because their tasks are blocked.

## Areas of Good Alignment

- Spring Boot and Vue are kept in separate runtime boundaries.
- H2 is local/test-only; the dev profile requires Oracle configuration.
- Flyway owns schema creation and Hibernate uses validation only.
- Catalog results expose only published seeded content and implement IBM i aliases.
- Lifecycle requests enforce scope shape and idempotency conflict handling.
- The fake lifecycle result explicitly says no files were changed.
- Unknown JSON properties fail closed, errors are sanitized, and correlation IDs are returned.
- Registration cannot advance beyond external validation without a real adapter.
- Feedback versions are checked against the requested Skill.
- Lifecycle, registration, and feedback writes create append-only audit events.

## Misalignments and Gaps

### Critical

None in the implemented local-only slice.

### Major

- **Trusted installer lifecycle is missing.** TASK-009 through TASK-012 require
  durable installation state, real progress, recovery, rollback, uninstall, and
  reconciliation. Current operations stop at a labelled fake success.
- **External integration paths are missing.** TASK-017 through TASK-022 cannot be
  implemented until the SSO, GitHub, artifact, installer, and certification
  contracts and credentials are approved.
- **Owner and feedback workflows are partial.** Preview/diff/publish, acknowledgement,
  reply, moderation, and post-action feedback linkage are not implemented.

### Minor

- Success responses are direct DTOs while errors use the accepted safe error shape;
  the project-local API guide permits this, although the generic architecture skill
  prefers a universal response envelope.
- Several small owner/feedback business operations remain in controllers and should
  move to services when those domains grow.
- Oracle migration execution has not been tested against a real Oracle instance.

## Coverage Check

| Design area | Status |
|---|---|
| Runtime scaffold and profiles | Implemented |
| Catalog discovery and detail | Implemented for local pilot |
| Security and object authorization | Partial; local auth and non-local deny-by-default |
| Lifecycle and installation state | Partial; fake operation only |
| Owner registration/publish | Partial; registration intake only |
| Feedback and moderation | Partial; submission/list only |
| Audit and observability | Partial; write audit plus health/metrics baseline |
| External SSO/GitHub/installer/certification | Missing and blocked |

Task coverage: TASK-001, TASK-002, TASK-003, TASK-005, TASK-006, TASK-007,
TASK-008, TASK-013, and TASK-015 are implemented or meaningfully partial.
TASK-004 and TASK-009 through TASK-016 are partial. TASK-017 through TASK-022
remain blocked as recorded in the task document.

## Architecture Review: Extensibility and Decoupling

### Score: 72%

### P0 (Must Fix)

None for continued local pilot testing. Real deployment remains prohibited until
the blocked trust-boundary integrations are implemented and reviewed.

### P1 (Fix Next Touch)

- Move Vue views and business state into `features/{domain}` stores; views currently
  call the shared API client directly.
- Extract registration and feedback transaction logic from controllers into domain services.
- Add Oracle-backed migration/contract CI before enabling the dev deployment profile.
- Expand the OpenAPI document to cover all implemented request and response schemas.

### P2 (Track)

- Centralize repeated backend API path strings when the contract surface expands.
- Split the compressed global stylesheet into shell and feature styles while preserving tokens.

## Readiness Verdict

- **Suitable for local pilot testing:** Yes
- **Suitable for dev/production deployment:** No
- **Required before deployment:** enterprise authentication, approved GitHub and
  installer contracts, Oracle verification, full lifecycle state/recovery behavior,
  and post-implementation security review of those adapters

## Minimal Next Fix Path

1. Resolve the SSO and trusted installer contracts and record ADRs.
2. Implement installation persistence/reconciliation and My Skills state.
3. Add owner preview/publish and feedback acknowledgement/moderation services.
4. Run Oracle integration tests and both post-implementation reviews again.
