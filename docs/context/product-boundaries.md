# Product Boundaries

## Positioning

Atlas Marketplace is an internal registry and lifecycle-management experience for
AI Skills whose source remains in distributed GitHub repositories.

The first product proof is one IBM iSeries RPGLE program-analysis Skill for a TL
reviewing and estimating changes. Company-wide employee access is permitted, while
ten first-time users form only the observed pilot cohort.

## MVP Lifecycle

```text
Consumer: Discover -> Detail -> Install -> My Skills -> Update/Rollback -> Uninstall
Owner:    Register -> Validate -> Preview -> Publish -> Sync
Platform: Authenticate -> Authorize -> Audit -> Operate
```

The pilot includes discovery, detail, Repository registration, Owner confirmation,
Metadata validation, version publishing, version-specific certification, Windows
installation into VS Code GitHub Copilot Chat, installation records, user-confirmed
updates, automatic/manual rollback, feedback, SSO/RBAC, and audit evidence.

The pilot supports Personal and Project installations. They may have different
versions, are updated independently, and Project takes precedence in that Project.
Only the immediately previous local version is retained for rollback.

Install may have a command fallback. A host must not be labelled one-click
capable unless a supported local component can complete its safe filesystem
operation and report the result.

## Day 2

- mature multidimensional quality scoring and health
- Leaderboard, Trending, and Most Installed
- Curated Collections
- Save/Favorite
- personalized recommendations beyond the pilot's empty-result related suggestions
- Owner adoption insights and lifecycle alerts
- governance beyond the pilot's RPGLE version certification and risk-feedback rules

Day 2 UI may display `Coming Soon`, but it must not show fixture/static data as
operational functionality or delay the pilot lifecycle.

## Explicit Non-Goals

- hosting Skill source code
- replacing GitHub or bypassing repository permissions
- online Skill execution, Agent Runtime, or workflow/playbook orchestration
- prompt playground or full source editing
- forced migration to one Skill monorepo
- public external distribution, payments, subscriptions, or settlement

## Product Source Precedence

1. Approved active feature spec
2. PRD v0.3 and approved product decisions
3. ADRs and architecture documents
4. Prototype and exploratory material
