# System Overview

## Logical Context

```text
Employee Browser
    |
    v
Marketplace Web ---- Company SSO
    |
    v
Registry API ---- Registry Store
    |  |  |       metadata, versions, installations, feedback, audit
    |  |  |
    |  |  +---- GitHub Integration ---- Owner Repositories
    |  +------- Artifact Resolver ----- Versioned Artifacts
    +---------- Lifecycle Orchestration / Job Progress
                     |
                     v
              Atlas Local Installer
                  |           |
                  v           v
           Copilot Adapter  OpenCode Adapter
```

This is a logical boundary map, not a microservice requirement. The Registry API
may begin as a modular monolith after the stack ADR, provided modules and
contracts preserve these responsibilities.

## Runtime Responsibilities

### Marketplace Web

- Discover, search, filter, Skill Detail, Register, My Skills, Owner, and Admin UI
- SSO session UX and role-aware presentation
- initiates approved lifecycle actions and displays durable job progress
- never evaluates arbitrary commands or writes local Skill files

### Registry API

- registry metadata, version, installation, feedback, Featured, and audit state
- authorization enforcement and GitHub permission-aware data filtering
- metadata validation, repository sync, publication, and lifecycle orchestration
- artifact eligibility and update job state, but not arbitrary local execution

### Atlas Local Installer

- obtains explicit user consent for local lifecycle actions
- owns canonical path checks, staging, artifact verification, backup, activation,
  post-install validation, rollback, uninstall, and local locking
- reports structured progress and result events to the orchestrator
- uses host adapters; it does not infer arbitrary behavior from metadata scripts

### Contracts And Host Adapters

- `packages/contracts/` owns versioned schemas that cross runtime boundaries
- `packages/host-adapters/` owns host capability interfaces and implementations
- contracts do not depend on web components, transport framework types, or
  persistence models

## Data Ownership

| Data | Authority |
|---|---|
| Skill source and repository access | GitHub repository |
| Registry metadata and publication status | Registry API/store |
| Installable artifact integrity metadata | Artifact manifest + approved source |
| Local active files and backups | Atlas Local Installer within authorized root |
| Installation and update history | Registry record reconciled with local result |
| Identity and group membership | Company SSO/identity provider |

## Initial Delivery Shape

Use a small number of deployable units: one web app, one Registry API, and one
local client. Do not split the API into services until measured scaling,
independent deployment, or isolation requirements justify it.
