# User Stories: RPGLE Skill Marketplace Pilot

## Status

Draft — migrated from the original Atlas feature specification on 2026-08-18.

## Actors

- **Consumer**: discovers, installs, invokes, updates, rolls back, uninstalls,
  rates, and reports issues.
- **AI SME / Owner / Primary Maintainer**: registers and maintains the RPGLE
  Skill, publishes versions, and responds to feedback.
- **Administrator**: governs access, taxonomy, ownership exceptions, and audit.
- **Platform**: authenticates, authorizes, validates, synchronizes, and records
  lifecycle evidence.

## Stories

### US-RPGLE-001: Discover and evaluate the RPGLE Skill

As a TL estimating an IBM iSeries change, I want to find the RPGLE analysis
Skill without knowing its name and evaluate concrete evidence, so that I can
decide whether it fits my task.

#### Acceptance Criteria

- IBM iSeries aliases such as `AS400`, `IBM i`, and `iSeries` resolve to the
  same canonical category and Skill.
- Results use truthful rating, certification, and relevance behavior.
- Detail shows supported program facts, dependencies, call relationships,
  program panorama, owner, version, access, and certification evidence.
- Empty results offer a recovery path instead of a dead end.

### US-RPGLE-002: Install and explicitly invoke the Skill

As a first-time Windows user, I want to install the Skill to Personal or Project
scope and explicitly invoke it in VS Code GitHub Copilot Chat, so that I receive
a useful RPGLE analysis report.

#### Acceptance Criteria

- Atlas guides local-component setup and resumes the original installation.
- Personal is the default scope; Project scope is independently selectable and
  takes precedence in that Project.
- Installation success includes a validated, copyable invocation command.
- Pilot success requires valid Host output, not only registry or file-copy
  success.
- Failures show a reason and manual Retry without silent retry.

### US-RPGLE-003: Review and safely apply updates

As a Consumer, I want to review and confirm updates per scope, recover from
technical failure, and restore the prior version after quality regression, so
that frequent releases do not strand or destabilize me.

#### Acceptance Criteria

- Atlas shows `Update Available` without modifying local files automatically.
- Confirmation includes target version, changelog, certification,
  compatibility, and scope.
- Technical failure restores the previous verified version and records why.
- Manual rollback restores only the immediately previous local version.
- Personal and Project installations remain independently updateable.

### US-RPGLE-004: Report and track a missed dependency

As a Consumer, I want to report a missed dependency against the affected Skill
Version and track the owner response, so that the Skill can improve without me
submitting source code.

#### Acceptance Criteria

- Feedback records Skill Version and installation scope automatically.
- Source, prompts, secrets, and unsafe attachments are not accepted.
- The submitter can see Open, Reviewed, and Resolved status plus owner replies.
- Ten qualifying independent reports suspend that version's certification.
- A fixed release uses the normal update experience.

### US-RPGLE-005: Publish and maintain the pilot Skill

As the AI SME, I want to register an existing GitHub repository, confirm
ownership, publish validated Git Tags, and synchronize releases, so that users
receive maintained versions without a platform content-approval bottleneck.

#### Acceptance Criteria

- Registration requires Repository Write/Maintain permission, confirmed Owner,
  and named Primary Maintainer.
- Git Tag is the Published Version authority and must match metadata version.
- New versions start Uncertified.
- Webhook and manual Refresh create traceable synchronization outcomes.
- Repository loss stops new installs without remotely deleting local installs.

### US-RPGLE-006: Respect visibility and ownership boundaries

As an employee, I want catalog and installation actions to respect repository
policy, so that discovery does not leak restricted metadata or bypass GitHub.

#### Acceptance Criteria

- Ordinary inaccessible Skills may expose only approved name/description plus
  `No access` and `Contact Owner`.
- Restricted/Hidden Skills disclose no metadata or artifacts to unauthorized
  users.
- Ownership transfer requires both current and new Owner confirmation.
- One year of owner inactivity triggers an audited administrator decision.

### US-RPGLE-007: Uninstall one managed scope safely

As a Consumer, I want to remove one Personal or Project installation, so that I
can exit safely without affecting another scope or unrelated project files.

#### Acceptance Criteria

- Confirmation identifies Skill, version, Host, and exact scope.
- Only managed content for the selected scope is removed.
- Interrupted or incomplete removal remains retriable and is not reported as
  success.

## Traceability

| Story | Requirement Groups | Detailed Specification |
|---|---|---|
| US-RPGLE-001 | REQ-RPGLE-001, REQ-RPGLE-006 | User Story 1; FR-001–FR-008, FR-036–FR-040 |
| US-RPGLE-002 | REQ-RPGLE-002, REQ-RPGLE-008, REQ-RPGLE-009 | User Story 2; FR-009–FR-015, SPR-*, CCR-* |
| US-RPGLE-003 | REQ-RPGLE-003, REQ-RPGLE-008, REQ-RPGLE-009 | User Story 3; FR-016–FR-022, SPR-*, CCR-* |
| US-RPGLE-004 | REQ-RPGLE-004, REQ-RPGLE-008 | User Story 4; FR-031–FR-034 |
| US-RPGLE-005 | REQ-RPGLE-005, REQ-RPGLE-006, REQ-RPGLE-009 | User Story 5; FR-023–FR-030, FR-035–FR-040 |
| US-RPGLE-006 | REQ-RPGLE-006, REQ-RPGLE-008 | User Story 6; FR-035–FR-040, SPR-* |
| US-RPGLE-007 | REQ-RPGLE-007, REQ-RPGLE-008 | User Story 7; FR-041, SPR-* |
