# Atlas Marketplace Project Rules

Hard rules for contributors and coding agents. Detail belongs in linked docs;
this file remains concise and enforceable.

## Source Of Truth

Use this precedence when documents disagree:

1. Accepted specification under `docs/03-spec/` for the active slice
2. Approved product decisions and PRD in `docs/product/`
3. ADRs in `docs/architecture/decisions/`
4. Architecture and standards documents
5. Prototype visuals and exploratory notes

The prototype demonstrates visual direction and partial Discover behavior. It
does not override the PRD, define backend behavior, or move Day 2 features into
MVP.

## SDD Workflow Gate

Atlas operates in the profile defined by `docs/00-context/sdd-profile.md`.
Non-trivial or user-facing changes must travel through the `docs/01`–`docs/06`
SDD chain before implementation. The slice must have accepted requirements,
user stories, specification, architecture, design, tasks, and applicable
contracts, checklists, reviews, and traceability evidence.

The project-local SDD skills under `.agents/skills/` are an exact mirror of the
authoritative `Agentic-SDLC-Control-Tower/.claude/skills/` directory. Use only
the skills listed in `docs/00-context/agentic-sdlc-registry.md`, and use the
profile plus `docs/SDD-BOOTSTRAP.md` to route them. Do not introduce a second
feature, requirements, design, task, or workflow-skill family.

For a full SDD pass, apply the available stage skills in document order and
report the exact skill chain, ADR result, and `review-doc-quality` result. Do
not name or require orchestration, profile-manager, freshness, manifest, or
other skills that are absent from the authoritative source.

If implementation already exists without matching SDD artifacts, backfill the
slice and mark the documents `Backfilled`; never imply that they preceded the
implementation. Code, tests, contracts, and user-facing documentation must be
traceable to the slice. Project rules and SDD artifacts are English-only; do not
create bilingual SDD companions unless explicitly requested.

## Product Discovery Protocol: Grill Mode

Use Grill Mode before specification or planning when the user's product intent
is incomplete, vague, internally inconsistent, or explicitly presented for
questioning or stress-testing.

### Triggers

Enter Grill Mode when the user says or clearly means any of the following:

- "grill me";
- help me clarify or think through this product;
- interrogate, challenge, pressure-test, or find blind spots in this idea;
- ask me questions before writing the PRD, spec, plan, or implementation.

### Interview Rules

1. **Ask one focused question per turn.** Wait for the answer before moving on.
2. **Walk the decision tree depth-first.** Select the most load-bearing open
   branch, drill until it is resolved or explicitly deferred, then backtrack.
3. **Read before asking.** If the PRD, prototype, repository, existing spec, ADR,
   or code can answer a question, inspect it and use that evidence to ask a
   sharper question instead of delegating lookup work to the user.
4. **Demand observable specificity.** Push vague words such as "easy", "smart",
   "internal users", "quality", "one-click", or "successful" toward a concrete
   actor, trigger, current behavior, desired behavior, constraint, and measurable
   outcome.
5. **Interrogate intent before implementation.** Establish the problem, user,
   current alternative, urgency, behavior change, value, scope, non-goals,
   success criteria, adoption, and product risks before asking technical
   questions. Do not ask the user to choose implementation details that can be
   safely decided later from evidence and best practice.
6. **Challenge rather than validate prematurely.** Test whether the problem is
   real, whether Marketplace is the right intervention, why current alternatives
   are insufficient, and what evidence could disprove the idea.
7. **Cover neglected branches.** Where relevant, examine constraints, edge cases,
   failure/recovery, permissions, privacy/legal concerns, operations, ownership,
   rollout, adoption, incentives, support, cost, timeline, team capacity, and
   second-order consequences.
8. **Do not answer your own question.** A response frame or example shape is
   allowed, but do not steer the user into accepting an invented product decision.
9. **Stay read-only.** Do not edit files, create SDD artifacts, scaffold code,
   or start implementation while Grill Mode is active.
10. **Recognize questions conversation cannot settle.** If a decision depends on
    seeing or using an interaction, mark it for a throwaway prototype or test
    rather than debating it indefinitely.

### Session Checkpoints

After roughly five to eight substantive answers, or when finishing a major
branch, provide a compact checkpoint containing only:

- decisions resolved and the user's stated reason;
- decisions explicitly deferred;
- open branches;
- the branch currently being examined.

A checkpoint is orientation, not a proposal. Resume with one question.

### Termination And Handoff

End Grill Mode when all meaningful branches are resolved or explicitly deferred,
or when the user says to stop. Then provide a final decision summary containing
resolved decisions, deferred items, remaining risks, and evidence still needed.

Do not automatically turn the summary into project artifacts. Ask for explicit
confirmation before handing it to the SDD workflow, updating the PRD, writing an ADR, or
starting implementation. Once confirmed, use the same conversation as source
context rather than making the user repeat the answers.

## Change Classification

A change is non-trivial if it alters user-visible behavior, data, an API or
protocol, a security boundary, dependencies, deployment, or architecture. Such
changes require a complete SDD slice before implementation.

Copy edits, comments, formatting, and metadata-only cleanup may proceed directly
when scope and behavior do not change.

Create or amend an ADR before choosing or changing:

- runtime frameworks, persistence, messaging, or deployment topology;
- the local execution component or browser-to-client protocol;
- version authority, artifact distribution, signing, or retention policy;
- authentication, authorization, GitHub integration, or data ownership;
- shared conventions that affect more than one runtime.

## Product Scope

Atlas Marketplace MUST remain a centralized registry over distributed Skill
repositories. It MAY cache or host versioned distribution artifacts when they
remain traceable to their source. It MUST NOT become a source-code host.

Out of scope unless the PRD and constitution are deliberately amended:

- online Skill execution or Agent Runtime;
- workflow/playbook orchestration or Prompt Playground;
- source editing inside Marketplace;
- forced migration of Skills into one monorepo;
- external public marketplace, payments, subscriptions, or settlement.

Day 2 capabilities such as Leaderboard, Collections, Save, Recommendations,
Owner Insights, and mature quality scoring must not delay or masquerade as the
MVP lifecycle:

`Discover -> Detail -> Install -> My Skills -> Update/Rollback -> Uninstall`

and:

`Register -> Validate -> Preview -> Publish/Sync`

## Runtime Boundaries

- `marketplace-web` presents data and initiates approved operations.
- `registry-api` owns registry state and orchestration records; it does not write
  arbitrary user-local files.
- `atlas-installer` is the trusted local boundary for filesystem operations.
- Host-specific paths and behavior are implemented through allowlisted adapters.
- Cross-boundary payloads live in `packages/contracts/` and are versioned.
- GitHub remains the final authorization boundary for private repository content.

## Security And Privacy

- Validate every external input, including metadata, GitHub payloads, artifact
  manifests, API requests, and local-client messages.
- Never evaluate install/update/uninstall command strings from metadata as an
  unrestricted shell command. Prefer structured operations and allowlisted
  executables/arguments.
- Canonicalize and authorize paths before access. Defend against traversal,
  Zip Slip, symlink escape, archive bombs, and unsafe deletion.
- Verify artifacts before activation using a checksum or signature and a binding
  to repository, commit/tag, version, and host compatibility.
- Update uses staging, preflight validation, atomic rename/switch where supported,
  post-install validation, backup retention, and automatic rollback.
- Lifecycle jobs are idempotent, concurrency-locked, restart-safe, and audited.
- Do not store or emit private source code, prompt contents, access tokens, or
  secrets in registry data, analytics, logs, errors, or client telemetry.
- Render metadata as untrusted content; sanitize HTML/Markdown and prevent XSS.

Security-sensitive implementation must include abuse cases and failure-path
tests in its specification, architecture, design, and tasks.

## Coding And Contract Rules

- Prefer the simplest design that satisfies the current approved scope.
- Use explicit types and schema validation; avoid `any` at system boundaries.
- Use immutable updates and explicit state transitions.
- Keep UI, transport, orchestration, persistence, and vendor/host adapters
  separate. UI components do not own auth or persistence policy.
- External integrations and filesystem behavior are behind replaceable adapters.
- Do not use a generic `version` where the domain means repository metadata,
  published, latest, installed, previous, or target version.
- APIs use one consistent success/error contract once ADR-approved.
- Lockfiles are committed and changed with dependency changes after the package
  manager is selected.

## Verification Rules

- Behavior changes require tests at the lowest useful level plus integration or
  contract coverage at changed boundaries.
- Critical install/update/rollback/uninstall flows require failure injection,
  interruption, idempotency, concurrency, and path-safety coverage.
- User-visible critical flows require E2E coverage once the runtimes exist.
- Accessibility targets WCAG 2.1 AA for keyboard, focus, labels, semantics,
  contrast, and non-color-only status communication.
- Performance budgets from the PRD must be represented in plans and verified for
  changes that can affect them.
- Never claim a check passed unless the command was actually run successfully.

## Documentation And Language

- Product documents may be Chinese; code, identifiers, schemas, protocol fields,
  ADR titles, and normative technical rules use English.
- Do not create duplicate bilingual documents by default.
- Specs define change scope; ADRs record cross-cutting decisions and rationale;
  standards define repeatable implementation conventions.
- Keep open decisions explicitly marked in `docs/context/open-questions.md`.
