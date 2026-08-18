# SDD Generation Gate Checklist

Use this checklist before accepting a newly generated or materially updated
Atlas Marketplace SDD slice.

## Required Evidence

The completion report must include:

- `SDD skill chain used: yes`
- Authoritative project-local skill(s) used, with paths, for every applicable
  stage
- ADR created/updated, or an explicit `not applicable` reason
- `review-doc-quality` result or an explicit blocked reason
- `verify-sdd-skills.sh` result when the mirrored skills or source lock changed

## Gate Checklist

| Check | Pass Criteria |
|---|---|
| Goal contract | Goal, slice, scope, exclusions, acceptance, verification, and constraints are explicit |
| Required context | Project rules, constitution, SDD profile, active slice, boundaries, standards, and relevant ADRs were read |
| SDD chain integrity | Required artifacts live under `docs/01-requirements/` through `docs/06-tasks/`; no parallel feature workflow exists |
| Skill chain | The applicable project-local skills were used rather than generating the set ad hoc |
| Artifact naming | Atlas slice-prefixed paths were used; no generic parallel artifact was created |
| Skill ownership | Overlapping generation and implementation skills have one explicit owner for each output or task set |
| Skill mirror | For skill-source changes, the local mirror matches the locked upstream commit |
| Language | Project rules and SDD artifacts are English-only unless explicitly requested otherwise |
| Spec quality | Happy path, empty/error states, acceptance scenarios, and measurable outcomes are present |
| Product boundaries | Registry/source/installer/host ownership and out-of-scope behavior are explicit when relevant |
| Security and audit | Validation, authorization, provenance, privacy, audit, and recovery expectations are explicit when relevant |
| Contracts | Boundary schemas, protocol versions, compatibility, and unsupported-host behavior are covered when relevant |
| Tasks | Tasks are ordered, scoped, verifiable, and mapped to the spec/plan |
| Traceability | Sources → requirements → stories → spec → plan/design → tasks → verification |
| Grounding | Existing-code claims are verified or tagged `[UNVERIFIED]`, `[ASSUMPTION]`, or `[USER-STATED]` |

## Fail Fast

Block implementation handoff if:

- `SDD skill chain used` is missing or `no`;
- required project-local skills were unavailable and SDD was generated anyway;
- spec, plan, and tasks disagree on scope;
- a required ADR or security decision is missing;
- upstream artifacts changed after downstream work without revalidation.
