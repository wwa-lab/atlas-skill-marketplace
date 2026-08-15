# AGENTS.md

This file is the entry point for coding agents working in Atlas Marketplace.
Keep it short; detailed and durable rules live in the linked documents.

## Required Reading

Before non-trivial work, read in this order:

1. `PROJECT_RULES.md`
2. `.specify/memory/constitution.md`
3. The relevant feature directory under `specs/`
4. `docs/context/product-boundaries.md` and `docs/context/glossary.md`
5. `docs/domain/README.md` when changing entities or lifecycle states
6. The standards for the affected runtime under `docs/standards/`
7. Relevant ADRs under `docs/architecture/decisions/`

Read `docs/product/Atlas_Marketplace_PRD_v0.3.md` when a change affects product
scope or acceptance criteria. Treat the HTML prototype as a visual reference,
not as proof that a feature is in MVP or already implemented.

## Product Discovery / Grill Mode

Automatically enter Grill Mode when the user asks to clarify, interrogate,
stress-test, or "grill" a product idea, product direction, feature, or plan.
Follow the complete protocol in `PROJECT_RULES.md`.

During Grill Mode:

- inspect existing project context before asking anything the repository can
  answer;
- ask one focused question per turn and walk the decision tree depth-first;
- challenge vague language, assumptions, trade-offs, failure modes, and missing
  constraints;
- remain read-only and do not create a plan, spec, tasks, or implementation;
- stop only when branches are resolved/deferred or the user asks to stop;
- ask before converting the outcome into Spec Kit artifacts.

## Change Workflow

- Non-trivial or user-visible changes use Spec Kit under `.specify/` and
  `specs/`. The feature spec is the single source of change intent.
- Architecture, security-boundary, data-ownership, protocol, or stack decisions
  require an ADR before implementation.
- Trivial copy, comment, formatting, and metadata fixes may be made directly
  when they do not alter behavior, scope, contracts, or data.
- Do not create a second requirements/design/task chain beside Spec Kit.
- Record unresolved choices in `docs/context/open-questions.md`; do not present
  them as decided architecture.

## Product And Architecture Boundaries

- Marketplace is a centralized registry over distributed GitHub repositories.
- Source code remains in owner repositories. Registry data is metadata, version,
  installation, feedback, audit, and approved analytics data.
- Online Skill execution, agent runtime, workflow orchestration, prompt
  playgrounds, public commerce, and a forced Skill monorepo are out of scope.
- Keep the web app, registry API, and local installer as separate trust and
  deployment boundaries.
- Host-specific behavior belongs behind host adapters.
- The browser and Registry API must never perform arbitrary local file or shell
  operations on behalf of a user.

## Intended Repository Layout

- `apps/marketplace-web/` — Marketplace browser UI
- `services/registry-api/` — Registry, GitHub sync, versions, feedback, audit
- `clients/atlas-installer/` — trusted local install/update/rollback client
- `packages/contracts/` — versioned schemas and cross-boundary protocols
- `packages/host-adapters/` — host adapter interfaces and implementations
- `specs/` — one Spec Kit directory per non-trivial feature/change
- `docs/` — product source, durable context, architecture, ADRs, standards
- `tests/contract/` — cross-boundary contract verification
- `tests/e2e/` — critical user journeys spanning runtimes
- `infra/` — local/deployment infrastructure after an ADR approves it

Do not add empty shared packages or services speculatively. A README documents
the intended boundary until a real feature needs code there.

## Safety Rails

Never:

- commit secrets, credentials, private source code, prompts, production exports,
  local install directories, artifacts, backups, or runtime logs;
- execute installation commands constructed from untrusted metadata;
- allow file writes, renames, extraction, or deletion outside an authorized and
  canonicalized Skill root;
- bypass GitHub repository permissions or claim one-click support for a host
  without a supported local execution path;
- mix repository, published, latest, installed, and rollback version concepts.

Always:

- validate external data at system boundaries with versioned schemas;
- sanitize rendered metadata and defend against command injection, path
  traversal, Zip Slip, and symlink escape;
- make install/update/rollback/uninstall operations idempotent, locked,
  auditable, recoverable, and safe under interruption;
- trace distribution artifacts to repository, commit or tag, version, and
  checksum or signature;
- use immutable data updates and explicit state transitions;
- show the diff and run relevant verification before committing.

## Verification

Application build commands are intentionally not invented before stack ADRs and
runtime scaffolding exist. Until then, use:

```sh
git diff --check
if rg -n '\[(PROJECT_NAME|PRINCIPLE_[0-9]+_[A-Z_]+|SECTION_[0-9]+_[A-Z_]+|GOVERNANCE_RULES|CONSTITUTION_VERSION|RATIFICATION_DATE|LAST_AMENDED_DATE)\]' .specify/memory/constitution.md; then
  echo "unresolved constitution placeholders found" >&2
  exit 1
fi
```

When runtimes are scaffolded, update this section and the relevant standards in
the same change with exact install, lint, typecheck, test, build, and E2E commands.

<!-- SPECKIT START -->
For an active Spec Kit feature, the feature plan supplies its concrete technology,
source layout, commands, and verification requirements.
<!-- SPECKIT END -->
