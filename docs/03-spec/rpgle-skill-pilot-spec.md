# Specification: RPGLE Skill Marketplace Pilot

**Slice**: `rpgle-skill-pilot`
**Created**: 2026-08-15
**Status**: Draft
**Migrated to SDD chain**: 2026-08-18
**Input**: Deliver the first end-to-end Atlas pilot in which an employee can
discover, evaluate, install, invoke, update, roll back, and give feedback on an
IBM iSeries RPGLE program-analysis Skill, while its AI SME can publish and
maintain versioned releases.

## Scope Classification *(mandatory)*

- **Product stage**: MVP Pilot
- **Primary lifecycle**: Consumer (primary); Owner, Admin, and Platform
  capabilities required to supply and govern the pilot Skill
- **Affected trust boundaries**: Web, Registry API, GitHub, Artifact source,
  Local installer, Host root
- **Source references**:
  - `docs/product/Atlas_Marketplace_PRD_v0.3.md`
  - `docs/context/product-boundaries.md`
  - `docs/architecture/security-boundaries.md`
  - `docs/product/prototypes/atlas_marketplace_v2_6_mvp_plus_day2.html`
    (visual reference only)
- **Explicit non-goals**: semantic search; macOS/Linux support; hosts other than
  VS Code GitHub Copilot Chat; silent background updates; online Skill
  execution; multiple retained backups; source or prompt upload; Day 2
  Leaderboard, Trending, Collections, Favorites, recommendations, or Owner
  analytics

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Discover and evaluate the RPGLE Skill (Priority: P1)

As a TL estimating an IBM iSeries change, I can find the RPGLE program-analysis
Skill without knowing its name and determine from concrete evidence whether it
fits my task.

**Why this priority**: The primary current failure is that users do not know a
relevant Skill exists or where it is. No later lifecycle has value until this is
solved.

**Independent Test**: Starting from the Discover page, a first-time employee can
use the IBM iSeries category or an AS400/IBM i/iSeries keyword, open the matching
Skill, and explain its use case, example output, current version, Owner, and
certification state without external help.

**Acceptance Scenarios**:

1. **Given** the RPGLE Skill is published under the canonical `IBM iSeries`
   category, **When** a user searches `AS400`, `IBM i`, or `iSeries`, **Then** the
   same Skill is returned through platform-managed aliases.
2. **Given** multiple matching Skills have ratings, **When** results are first
   displayed, **Then** higher-rated results appear before lower-rated results.
3. **Given** a matching Skill has no rating, **When** it is ranked among other
   unrated results, **Then** certified versions rank before uncertified versions
   and relevance resolves the remaining order.
4. **Given** no exact result exists, **When** the search completes, **Then** the
   user sees related Skills or adjacent categories and can clear filters.
5. **Given** the user opens the RPGLE Skill, **When** they review its detail,
   **Then** they see a curated report containing supported high-level program
   information, call relationships, dependencies, and a program panorama.
6. **Given** the current version has a certification record, **When** the user
   opens certification details, **Then** they see the SME, tested version, test
   date, and conclusion.

---

### User Story 2 - Install and explicitly invoke the Skill (Priority: P1)

As a first-time user on Windows, I can install the Skill to my chosen scope and
explicitly invoke it in VS Code GitHub Copilot Chat to obtain a useful RPGLE
analysis report.

**Why this priority**: Discovery alone does not validate the product. A completed
local install and valid Host output are the pilot's value endpoint.

**Independent Test**: A user with no Atlas local component selects the Skill,
installs the component through the guided flow, resumes the original install,
copies the shown invocation command, and receives one valid RPGLE report in the
target Host without repeating discovery.

**Acceptance Scenarios**:

1. **Given** an eligible Windows user has not installed the local component,
   **When** they select Install, **Then** Atlas guides component installation and
   automatically resumes the same Skill installation after the component is
   ready.
2. **Given** the installation dialog is open, **When** the user makes no scope
   change, **Then** Personal is selected by default; the user can instead select
   a Project scope.
3. **Given** multiple published versions are eligible, **When** the user selects
   a version, **Then** Latest is the default and allowed older versions remain
   selectable.
4. **Given** the same Skill is installed personally and in the active Project,
   **When** the Host resolves it in that Project, **Then** the Project installation
   takes precedence.
5. **Given** installation succeeds, **When** Atlas shows the final result,
   **Then** it includes a copyable, validated invocation command naming the Skill.
6. **Given** the user copies and enters the command in VS Code GitHub Copilot
   Chat, **When** the Skill analyzes the standard pilot input, **Then** it returns
   a report with program high-level information and dependency/call relationships,
   not only help text or an error.
7. **Given** installation fails, **When** the terminal result is shown, **Then**
   the user sees an understandable reason and a manual Retry action, and Atlas
   does not silently retry or create a support ticket.

---

### User Story 3 - Review and safely apply updates (Priority: P2)

As a Consumer, I can update each installed scope after reviewing the release,
recover automatically from technical failure, and manually return to the prior
version if output quality regresses.

**Why this priority**: The AI SME expects roughly two releases per week, so a
pilot without an update and rollback loop would quickly strand users on tuned-out
versions.

**Independent Test**: Publish a higher version, update one selected scope, inject
one supported technical failure to verify automatic restoration, then complete a
successful update and manually roll it back for a simulated quality regression.

**Acceptance Scenarios**:

1. **Given** a higher eligible version is published, **When** the user views Atlas,
   **Then** the affected installation scope shows `Update Available` without
   updating automatically.
2. **Given** the user begins an update, **When** the confirmation is shown,
   **Then** they see the target version, changelog, certification status, and
   compatibility before choosing a scope and confirming.
3. **Given** download, integrity validation, file replacement, or Host loading
   fails, **When** the update terminates, **Then** the previous installation is
   restored and Atlas states that the update failed, the old version was restored,
   and why.
4. **Given** an update technically succeeds but output quality regresses, **When**
   the user selects manual rollback, **Then** only the immediately previous local
   version is restored.
5. **Given** the user manually rolled back version `N`, **When** Atlas checks for
   updates again, **Then** version `N` is suppressed for that installation until
   the Owner publishes a version greater than `N`.
6. **Given** Personal and Project installations differ, **When** the user updates
   one scope, **Then** the other scope's installed version remains unchanged.

---

### User Story 4 - Report and track a missed dependency (Priority: P2)

As a Consumer, I can report that a specific Skill Version missed an RPGLE
dependency and track the AI SME's response without submitting source code.

**Why this priority**: Missed dependencies are the most harmful known failure and
must create a version-specific, auditable improvement loop.

**Independent Test**: From an installation in My Skills, submit a missed-dependency
report, verify its automatically associated version and scope, and move it through
Open, Reviewed, and Resolved with an Owner response.

**Acceptance Scenarios**:

1. **Given** a user opens feedback from Skill Detail or My Skills, **When** they
   submit text and an optional program/repository identifier, **Then** Atlas
   automatically records the Skill Version and installation scope.
2. **Given** a feedback form is used, **When** the user attempts to attach source,
   prompts, or secrets, **Then** the product does not accept such content as an
   attachment or required field and explains the safe reporting boundary.
3. **Given** a missed-dependency report is submitted, **When** the AI SME reviews
   it, **Then** the submitter can see status changes and Owner replies, with the
   initial response measured against a two-business-day SLA.
4. **Given** a fix is published, **When** the submitter returns to Atlas, **Then**
   they receive the same normal `Update Available` experience as other users,
   rather than a special fixed notification.
5. **Given** ten independent missed-dependency reports accumulate for one version
   across its lifecycle, **When** the tenth is accepted, **Then** that version's
   certification is suspended and the reason is auditable.

---

### User Story 5 - Publish and maintain the pilot Skill (Priority: P2)

As the AI SME, I can register my existing GitHub Repository, obtain Owner
confirmation, publish validated tagged versions, and keep Atlas synchronized
without waiting for platform content approval.

**Why this priority**: Consumer update frequency depends on a low-friction Owner
release path with explicit accountability.

**Independent Test**: A registrant with Repository Write/Maintain permission
registers a repository, receives Owner confirmation, publishes a validated Git
Tag, and later synchronizes a new tag through both webhook and manual refresh.

**Acceptance Scenarios**:

1. **Given** a registrant lacks Repository Write/Maintain permission, **When** they
   attempt registration, **Then** publication is denied.
2. **Given** a registrant enters an Owner team, **When** registration continues,
   **Then** a named Primary Maintainer is also required and the named Owner must
   confirm before first publication.
3. **Given** Repository Metadata and the Git Tag pass validation, **When** the AI
   SME clicks Publish after Owner confirmation, **Then** the version is visible
   immediately without platform human approval.
4. **Given** Metadata declares a version different from the Git Tag, **When**
   validation runs, **Then** publication is blocked with a clear mismatch error.
5. **Given** a new version is published, **When** it first appears, **Then** it is
   Uncertified even if the prior version was Certified.
6. **Given** the Repository changes, **When** its webhook is delivered or the Owner
   selects Refresh, **Then** Atlas evaluates the observed revision once and records
   the sync result.
7. **Given** the Repository is deleted or becomes inaccessible, **When** Atlas
   detects it, **Then** the Skill page remains marked `Repository Unavailable`, new
   installs stop, and existing local installations are not remotely deleted.

---

### User Story 6 - Respect visibility and ownership boundaries (Priority: P3)

As an employee, I see only the Skill information and installation actions allowed
by the underlying Repository policy, while maintainers can transfer ownership
through explicit consent.

**Why this priority**: Enterprise-wide discovery must not leak sensitive Skill
Metadata or bypass GitHub access.

**Independent Test**: Compare an authorized and unauthorized employee against a
normal internal Skill and a Restricted/Hidden Skill, then complete an Owner
transfer with both parties' confirmation.

**Acceptance Scenarios**:

1. **Given** a normal company-visible Skill but no Repository access, **When** an
   employee views its Catalog entry, **Then** they see its name and description,
   a `No access` label and `Contact Owner`, but cannot install or fetch an Artifact.
2. **Given** a Restricted/Hidden Skill and no Repository access, **When** an
   employee searches or opens its URL, **Then** no Skill Metadata is disclosed.
3. **Given** an ownership transfer is proposed, **When** only one party confirms,
   **Then** the existing Owner remains; the transfer completes only after current
   and new Owners both confirm.
4. **Given** the Owner has been inactive for less than one year, **When** the Skill
   is viewed, **Then** it remains visible and installable with an inactivity warning.
5. **Given** inactivity reaches one year, **When** governance review runs, **Then**
   a platform administrator must choose transfer or delisting and the action is
   audited.

---

### User Story 7 - Uninstall one managed scope safely (Priority: P3)

As a Consumer, I can remove one Personal or Project installation without affecting
another scope or unrelated Project content.

**Why this priority**: Uninstall completes the managed local lifecycle and provides
a safe exit, but it is not part of the pilot's primary success metric.

**Independent Test**: Install the same Skill personally and in one Project, remove
the Project installation, and verify that the Personal installation and unrelated
Project files remain intact.

**Acceptance Scenarios**:

1. **Given** a user selects Uninstall for one scope, **When** confirmation appears,
   **Then** it identifies the Skill, installed version, Host, and exact scope.
2. **Given** the user confirms Project uninstall, **When** it completes, **Then**
   only managed content for that Project installation is removed and the Personal
   installation remains available.
3. **Given** uninstall is interrupted or cannot remove all managed content, **When**
   the job reconciles, **Then** it reports an incomplete/retriable state rather than
   success and does not delete unrelated files.

### Edge Cases

- Duplicate Install or Update requests with the same idempotency identity do not
  create two active jobs or two copies of the Skill.
- Concurrent operations on the same installation scope are serialized or one is
  rejected without corrupting the active version.
- A browser closes or the local component restarts after consent; the job either
  resumes safely or reconciles to a clear retriable terminal state.
- An Artifact checksum/signature fails before replacement; the active installation
  remains untouched.
- A process terminates after old-version backup but before activation; recovery
  restores one valid active version and records the outcome.
- A Retry after failure is safe and does not duplicate registry or local state.
- Malicious Metadata includes HTML/script, shell fragments, absolute paths,
  traversal segments, oversized fields, or invalid versions; it is rejected or
  safely rendered and never executed.
- An archive includes `..` entries, absolute paths, symlinks escaping the Skill
  root, or duplicate conflicting paths; extraction fails without out-of-root writes.
- Repository access is revoked between page view and Artifact request; the
  server rechecks access and denies the request.
- An installed version becomes Repository Unavailable; it stays local, while
  update and reinstall actions explain why they cannot proceed.
- A version is Uncertified, Suspended, or has overdue high-risk feedback; install
  remains possible only after an obvious risk warning is displayed.
- A rating is absent; the interface says it is unrated rather than substituting a
  fabricated score.
- Ten reports are submitted by the same user or are duplicates; only reports
  satisfying the independent-feedback policy count toward certification suspension.
- The local component is outdated; Atlas can require a user-confirmed component
  update before safely resuming the requested Skill operation.
- The Host does not recognize the installed Skill or invocation syntax; the pilot
  records the invocation as failed rather than treating file copy as success.

## Requirements *(mandatory)*

### Functional Requirements

#### Discovery and evaluation

- **FR-001**: The system MUST provide `IBM iSeries` as the pilot's canonical
  platform category and map platform-managed aliases including `AS400`, `IBM i`,
  and `iSeries` to it.
- **FR-002**: Owners MUST be able to add free tags without creating or modifying
  canonical categories or aliases.
- **FR-003**: Users MUST be able to combine category filtering with ordinary
  keyword search across name, description, use case, tag, and alias fields.
- **FR-004**: Search MUST use user rating as the primary default ordering for rated
  Skills; unrated Skills MUST use certification status before search relevance.
- **FR-005**: An empty result MUST offer adjacent categories or related Skills and
  a way to clear filters.
- **FR-006**: Each Skill Card MUST show name, description, Owner/Primary Maintainer,
  category/tags, concrete use cases, latest version, version certification state,
  a curated output preview, access state, and truthful rating state.
- **FR-007**: RPGLE Skill Detail MUST show one curated report containing supported
  high-level program facts, purpose, call relationships, dependency list, and a
  program panorama.
- **FR-008**: Certification detail MUST identify the SME, tested version, test date,
  and conclusion.

#### Installation, invocation, and lifecycle

- **FR-009**: Installation MUST support Personal and Project scopes, default to
  Personal, and give the active Project installation precedence over Personal.
- **FR-010**: Users MUST be able to select Latest by default or an older eligible
  Published Version.
- **FR-011**: If the local component is absent, Atlas MUST guide its installation
  and resume the original Skill install when readiness is confirmed.
- **FR-012**: The install experience MUST show Loading and a clear terminal result;
  failures MUST provide a reason and manual Retry without silent automatic retry.
- **FR-013**: Successful installation MUST show a copyable, validated command that
  explicitly invokes the installed Skill by name in VS Code GitHub Copilot Chat.
- **FR-014**: Pilot success MUST require a valid Host output containing program
  high-level information and dependency/call relationships; registry or file-copy
  success alone is insufficient.
- **FR-015**: My Skills MUST merge a Skill into one card and expose each Personal
  or Project installation's version, state, certification, and actions when expanded.
- **FR-016**: A higher eligible version MUST create an Atlas-only `Update Available`
  state and MUST NOT change local files before user confirmation.
- **FR-017**: Update confirmation MUST show target version, changelog, certification,
  compatibility, and selected installation scope.
- **FR-018**: Each installation MUST retain only its immediately previous local
  version as a restorable backup.
- **FR-019**: Download, integrity, replacement, and Host loading failure MUST trigger
  automatic restoration; the result MUST name the failure and restored version.
- **FR-020**: Users MUST be able to manually restore the immediately previous
  version for quality regression; the rejected version MUST be suppressed for that
  installation until a higher version is published.
- **FR-021**: Personal and Project installations MUST be independently updateable.
- **FR-022**: A local component update MUST require user confirmation before it is
  applied.

#### Publishing, certification, and feedback

- **FR-023**: Registration MUST require Repository Write/Maintain permission, a
  manually named Owner, that Owner's confirmation, and a named Primary Maintainer.
- **FR-024**: Repository Metadata MUST be the primary descriptive source; Atlas
  fields MAY fill missing information without maintaining a competing full copy.
- **FR-025**: Git Tag MUST be the authoritative Published Version; any version in
  Metadata MUST match it.
- **FR-026**: After permission, Owner confirmation, and Metadata validation pass,
  an AI SME MUST be able to publish immediately without platform content approval.
- **FR-027**: GitHub webhook and Owner-triggered Refresh MUST both initiate
  traceable Repository synchronization.
- **FR-028**: Repository deletion or access loss MUST retain a marked page, pause
  new installs, and leave existing local installations untouched.
- **FR-029**: Certification MUST belong to one Skill Version and MUST be reset to
  Uncertified whenever a new version is published.
- **FR-030**: Uncertified, Suspended, and overdue-high-risk versions MUST remain
  installable only with an obvious risk warning.
- **FR-031**: Ten independent missed-dependency reports accumulated across one
  version's lifecycle MUST suspend that version's certification; fewer reports
  MUST NOT automatically suspend it.
- **FR-032**: Feedback MUST be submitable from Detail and My Skills, automatically
  associate version and installation scope, accept text and an optional program/
  repository identifier, and exclude source or prompt uploads.
- **FR-033**: A missed-dependency report MUST expose Open, Reviewed, and Resolved
  status plus Owner replies, and measure initial response against two business days.
- **FR-034**: A fixed version MUST use the ordinary Update Available experience,
  without a special targeted notification to the reporter.
- **FR-035**: Owner transfer MUST require confirmation by both current and new
  Owners; one year of Owner inactivity MUST trigger an audited administrator choice
  to transfer or delist while shorter inactivity only shows a warning.

#### Access, observability, and presentation

- **FR-036**: All company employees MUST be able to authenticate; the initial ten
  pilot participants MUST NOT receive a separate product path or authorization model.
- **FR-037**: For ordinary internal Skills, a user without Repository access MAY
  see name and description with `No access` and `Contact Owner`, but MUST NOT
  install or fetch an Artifact.
- **FR-038**: Restricted/Hidden Skill Metadata and Artifacts MUST be invisible to
  users lacking underlying Repository access.
- **FR-039**: The system MAY record search, browse, install, update, rollback, and
  feedback events but MUST NOT record source code, prompts, secrets, or unrestricted
  local paths.
- **FR-040**: Day 2 controls shown during the pilot MUST say `Coming Soon` and MUST
  NOT present fixture/static data as live operational evidence.
- **FR-041**: Users MUST be able to confirm and uninstall one selected Personal or
  Project installation without changing other scopes or deleting unmanaged files;
  interrupted or incomplete removal MUST remain visibly retriable.

### Key Entities *(include if feature involves data)*

- **Skill**: Catalog identity linked to one owner-controlled Repository, with
  Owner, Primary Maintainer, category, tags, visibility, description, use cases,
  and curated example report.
- **Skill Version**: Immutable published release identified by Git Tag and source
  revision, with changelog, compatibility, lifecycle, Artifact, and certification.
- **Certification**: One version-specific SME assessment with standard test-set
  identity, test date, conclusion, state, and suspension reason.
- **Distribution Artifact**: Installable content traceable to Repository, Git Tag,
  source revision, format, and integrity evidence.
- **Installation**: One user's Skill in one Personal or Project scope and Host,
  including installed/latest/previous versions, lifecycle state, and suppressed
  rejected version.
- **Lifecycle Job**: Idempotent install, update, rollback, or uninstall attempt with
  actor, scope, target, progress reconciliation, result, and recovery outcome.
- **Backup**: The immediately previous verified local installation retained for
  one installation scope.
- **Feedback**: Version- and scope-linked user report with risk type, safe optional
  program/repository identifier, status, Owner replies, and SLA timestamps.
- **Repository Sync**: One observed Repository revision and its trigger, validation,
  publication eligibility, and result.
- **Ownership Confirmation**: Evidence that the named Owner and Primary Maintainer
  accepted responsibility, plus any dual-consent transfer.
- **Audit Event**: Append-only evidence for sensitive lifecycle and governance
  actions, excluding private source, prompt, secret, and unsafe path content.

### Security And Privacy Requirements *(mandatory when a trust boundary changes)*

- **SPR-001**: The system MUST authorize Repository visibility, publication,
  Artifact retrieval, and installation eligibility at the server boundary for each
  sensitive request; cached UI state is not authorization evidence.
- **SPR-002**: All Repository Metadata, webhook data, rendered text, manifests,
  versions, paths, and archives MUST be schema-validated and handled to prevent
  XSS, command injection, path traversal, Zip Slip, and symlink escape.
- **SPR-003**: Secrets, source code, prompts, production exports, unrestricted local
  paths, and Artifact content MUST NOT enter analytics, feedback, ordinary logs, or
  audit context; required credentials MUST remain outside repository content.
- **SPR-004**: Install, update, rollback, and uninstall MUST be idempotent, locked per
  installation scope, safe under retry/concurrency/interruption, and reconciled to a
  verified terminal state.
- **SPR-005**: The browser and Registry API MUST NOT execute arbitrary local shell or
  file operations; the trusted local component MUST restrict operations to an
  authorized, canonicalized Host Skill root.
- **SPR-006**: A risk-warning acknowledgment MUST NOT weaken access control or
  Artifact integrity requirements and MUST be auditable with version and scope.

### Contract And Compatibility Requirements *(mandatory when boundaries change)*

- **CCR-001**: Cross-boundary Metadata, Artifact manifest, local lifecycle request,
  result, capability, and audit payloads MUST use explicitly versioned schemas with
  defined behavior for unsupported versions and unknown fields.
- **CCR-002**: Every distributed version MUST be traceable to Repository, Git Tag,
  source revision, and checksum or signature; mutable URLs or Metadata version alone
  MUST NOT establish provenance.
- **CCR-003**: Host-specific install roots, activation, precedence, loading checks,
  and invocation behavior MUST be exposed through a validated VS Code GitHub
  Copilot Chat capability contract; unsupported OS/Host combinations MUST fail before
  local mutation with a clear explanation.
- **CCR-004**: The browser-to-local flow MUST bind explicit user consent to one
  versioned operation and support safe readiness detection, resume, expiration,
  duplicate delivery, and result reconciliation.
- **CCR-005**: Rollout MUST preserve the previous local version until the target is
  integrity-checked and Host-loadable; rollback MUST restore that version without
  redefining Repository, Published, Latest, Installed, Target, or Previous versions.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: In live observation, at least 7 of 10 first-time target employees find
  and successfully install the RPGLE Skill within 10 minutes without asking a
  colleague or opening any extra operating document.
- **SC-002**: The same at least 7 participants explicitly invoke the named Skill in
  VS Code GitHub Copilot Chat and receive one valid report containing high-level
  program information plus dependency/call relationships within the same 10-minute
  session.
- **SC-003**: 100% of tested download, integrity, file-replacement, and Host-loading
  failures in the approved pilot fault set leave the previously installed version
  active and produce an auditable restoration result.
- **SC-004**: 100% of tested unauthorized Restricted/Hidden lookups and Artifact
  requests disclose no Skill Metadata or Artifact content.
- **SC-005**: 100% of tested new releases begin Uncertified and do not inherit a
  prior version's certification.
- **SC-006**: 100% of submitted pilot feedback records can be traced to Skill
  Version, installation scope, status, and Owner response history without storing
  source code or prompts.
- **SC-007**: 100% of tested install/update/rollback paths write only within the
  approved canonical Host root, including adversarial archive and symlink cases.

## Assumptions

- Pilot participants are company employees with company identity, GitHub Enterprise
  accounts, Repository access where required, Windows devices, VS Code, and GitHub
  Copilot Chat entitlement.
- Pilot device policy allows users to install the Atlas local component without a
  one-off administrator or IT approval.
- One Owner-maintained RPGLE analysis Skill and a standard non-sensitive RPGLE test
  set are available for the pilot.
- The AI SME expects approximately two Skill releases per week, making update and
  rollback part of pilot readiness rather than a later enhancement.
- A visible warning followed by user choice is the confirmed product policy for
  Uncertified, Suspended, or overdue-high-risk versions; certification is not an
  installation gate in this feature.
- Company-wide availability and ten-person observation can coexist: the ten users
  are a measurement cohort, not a separate tenant or entitlement group.

## Open Questions

These choices are deferred to research, Contract, or ADR work before implementation;
they do not change the user outcomes and acceptance behavior above:

- **OQ-001**: What officially supported VS Code GitHub Copilot Chat Skill directory,
  refresh/loading behavior, and explicit invocation syntax satisfy FR-013/FR-014?
- **OQ-002**: Should the trusted Windows component be a CLI, service, VS Code
  extension, or constrained combination?
- **OQ-003**: Which browser-to-local discovery, consent, resume, and reconciliation
  protocol satisfies CCR-004 without arbitrary command execution?
- **OQ-004**: Which GitHub Enterprise and company SSO mechanisms provide per-request
  Repository authorization and Owner Write/Maintain verification?
- **OQ-005**: Who builds and stores Distribution Artifacts, which integrity mechanism
  is required, and how are compromised releases revoked?
- **OQ-006**: What standard RPGLE program set, dependency truth set, and scoring rule
  define certification and a valid pilot report?
- **OQ-007**: How are duplicate/non-independent missed-dependency reports detected
  before counting toward the threshold of ten?
- **OQ-008**: What signed, compatible, recoverable mechanism updates the local
  component after user confirmation?

The working records for these questions live in
`docs/context/open-questions.md`; architecture-changing answers require ADRs before
implementation planning is considered complete.

## Upstream SDD Artifacts

- `docs/01-requirements/rpgle-skill-pilot-requirements.md`
- `docs/02-user-stories/rpgle-skill-pilot-user-stories.md`
- `docs/00-context/rpgle-skill-pilot-traceability.md`
