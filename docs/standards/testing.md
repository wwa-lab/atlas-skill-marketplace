# Testing Standard

Testing is proportional to change risk, but changed behavior must be verified.

## Test Layers

- **Unit**: domain rules, state transitions, validation, components, and adapters'
  pure logic.
- **Integration**: persistence, GitHub/SSO/artifact adapters with controlled fakes
  or test systems, local filesystem behavior in temporary roots.
- **Contract**: metadata schema, API, installer protocol, job progress/events,
  artifact manifest, and host capability compatibility.
- **E2E**: critical Consumer and Owner journeys across deployed runtimes.
- **Non-functional**: security abuse cases, accessibility, performance budgets,
  recovery, and operational observability.

## Change Expectations

- Bug fixes add a regression test that fails before the fix when practical.
- New behavior includes success, validation, permission, empty/error, and
  recovery cases relevant to the feature.
- Contract changes test supported old/new versions or state an intentional
  breaking migration.
- Lifecycle changes cover duplicate/retry/concurrency/interruption and every
  point after which rollback is required.
- External paid or sensitive services are replaceable with fakes; critical tests
  do not depend on production credentials or live private repositories.

## Coverage

Use coverage as a signal, not a substitute for critical-path assertions. The
initial target is at least 80% for executable code once tooling exists, with no
uncovered safety-critical state transition accepted solely because aggregate
coverage passes.

## Reporting

Document exact commands and their result. Do not claim checks that were not run.
When a check cannot run, report the reason and residual risk.
