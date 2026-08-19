# Data Model: RPGLE Skill Marketplace Pilot

## Status

Accepted for task planning on 2026-08-18.

## Overview

The Registry Store persists catalog projections, immutable published versions,
scope-specific reconciled installations, durable lifecycle work, feedback,
certification, repository synchronization, ownership evidence, and audit events.
It never stores Skill source, prompts, secrets, artifact contents, or arbitrary
local paths.

## Entity Relationship Diagram

```text
OWNER 1 ── N SKILL 1 ── N SKILL_VERSION 1 ── 1 DISTRIBUTION_ARTIFACT
                │               │  └── 0..N CERTIFICATION
                │               ├──── 0..N FEEDBACK ─── N FEEDBACK_REPLY
                │               └──── 0..N REPOSITORY_SYNC
                └── 0..N OWNERSHIP_CONFIRMATION

SKILL 1 ── N INSTALLATION 1 ── 0..1 BACKUP
                       └── 0..N LIFECYCLE_JOB

All sensitive transitions ── N AUDIT_EVENT
```

## Common Conventions

- Primary keys are application-generated UUIDs stored in a portable UUID/string
  mapping selected by the persistence adapter.
- Mutable aggregates carry `row_version` for optimistic concurrency.
- Timestamps are UTC instants. User-facing zones are presentation concerns.
- Enumerations are stable uppercase strings, never database ordinal positions.
- H2 and Oracle schemas are created only by Flyway migrations.
- Text length constraints apply before persistence; large safe text is stored as
  logical CLOB only where explicitly identified.

## Entity Definitions

### skill

| Column | Logical type | Null | Constraint / Meaning |
|---|---|---:|---|
| id | UUID | no | primary key |
| slug | String(100) | no | unique, lowercase stable catalog identity |
| display_name | String(160) | no | sanitized display text |
| description | String(2000) | no | sanitized plain/approved Markdown source |
| repository_ref | String(512) | no | provider-neutral repository identifier |
| owner_id | UUID | no | owner reference |
| primary_maintainer_ref | String(200) | no | identity reference, not secret |
| category_code | String(80) | no | managed taxonomy code |
| visibility | Enum | no | `INTERNAL`, `RESTRICTED`, `HIDDEN` |
| publication_state | Enum | no | `DRAFT`, `PUBLISHED`, `DEPRECATED`, `ARCHIVED` |
| curated_report | Text | yes | sanitized non-sensitive example output |
| last_repository_activity_at | Timestamp | yes | owner inactivity evidence |
| row_version | Long | no | optimistic lock |

Tags use `skill_tag(skill_id, tag_value)` with a unique pair. Aliases are
platform-managed in `taxonomy_alias(alias_normalized, category_code)`.

### skill_version

| Column | Logical type | Null | Constraint / Meaning |
|---|---|---:|---|
| id | UUID | no | primary key |
| skill_id | UUID | no | foreign key |
| git_tag | String(120) | no | authoritative published identifier |
| source_revision | String(128) | no | immutable commit/revision |
| semantic_version | String(64) | no | parsed comparable version |
| changelog | Text | no | sanitized content |
| publication_state | Enum | no | explicit lifecycle |
| published_at | Timestamp | yes | set on publish |
| row_version | Long | no | optimistic lock |

Unique constraints: `(skill_id, git_tag)`, `(skill_id, source_revision)`, and
`(skill_id, semantic_version)`.

### distribution_artifact

One row per `skill_version`: immutable locator, manifest schema version, content
length, media/format, checksum algorithm/value, Host compatibility descriptor,
availability state, and revocation timestamp/reason. Locator access is mediated;
signed URLs and tokens are never persisted in audit or ordinary logs.

### certification

One current record plus append-only assessment history per Skill Version. Fields:
SME identity reference, test-set identity/version, tested timestamp, conclusion,
state (`UNCERTIFIED`, `CERTIFIED`, `SUSPENDED`, `FAILED`), suspension reason,
qualifying report count, and optimistic version. A new Skill Version receives an
independent `UNCERTIFIED` record.

### installation

| Column | Logical type | Null | Constraint / Meaning |
|---|---|---:|---|
| id | UUID | no | primary key |
| actor_ref | String(200) | no | company identity reference |
| skill_id | UUID | no | foreign key |
| host_code | String(80) | no | capability identifier |
| scope_type | Enum | no | `PERSONAL`, `PROJECT` |
| project_ref | String(300) | conditional | required only for Project |
| installed_version_id | UUID | yes | last verified active version |
| latest_eligible_version_id | UUID | yes | registry projection |
| previous_version_id | UUID | yes | immediately previous restorable version |
| suppressed_version_id | UUID | yes | manual rollback suppression |
| state | Enum | no | explicit installation state |
| last_reconciled_at | Timestamp | yes | installer evidence time |
| row_version | Long | no | optimistic lock |

Unique active identity: actor + Skill + Host + normalized scope/project. No
unrestricted absolute local path is stored.

### lifecycle_job

Stores operation ID/idempotency key, installation, operation type, requested and
target versions, immutable artifact reference, consent reference/expiry, state,
attempt/lease fields, safe progress code, safe failure code/message, terminal
result, timestamps, and optimistic version. The unique idempotency key binds the
same actor, installation, operation, and intended target.

### backup

At most one per installation. Stores previous version identity, integrity value,
installer-local opaque handle, verified/restored timestamps, and state. The
opaque handle is not an unrestricted path and is meaningful only to the installer.

### feedback and feedback_reply

Feedback stores reporter, Skill Version, optional installation/scope, type,
plain text, optional safe program/repository identifier, state, independence
decision/reason, SLA due/response timestamps, and optimistic version. Replies
are append-only with author, safe body, and timestamp.

### repository_sync

Stores Skill, trigger (`WEBHOOK`, `MANUAL`), delivery/idempotency identifier,
observed revision/tag, metadata schema version, validation result/error codes,
publication eligibility, requester, and timestamps. Raw webhook bodies and
repository contents are not stored.

### ownership_confirmation

Stores Skill, confirmation purpose, current/new owner references, primary
maintainer, status, requested/confirmed timestamps, expiry, and audit correlation.

### audit_event

Append-only fields: UUID, occurred_at, actor_ref/type, action code, target type/id,
outcome, correlation ID, schema version, and an allowlisted safe context document.
Updates/deletes are prohibited to the application role.

## State Models

```text
Publication: DRAFT → PUBLISHED → DEPRECATED → ARCHIVED
Certification: UNCERTIFIED → CERTIFIED | FAILED
               CERTIFIED → SUSPENDED
               SUSPENDED → CERTIFIED | FAILED (new assessment)
Installation: NOT_INSTALLED → INSTALLING → INSTALLED
              INSTALLED → UPDATING → INSTALLED
              UPDATING → ROLLING_BACK → INSTALLED | INCOMPLETE
              INSTALLED → UNINSTALLING → UNINSTALLED | INCOMPLETE
Job: REQUESTED → READY → DISPATCHED → RUNNING
     RUNNING → SUCCEEDED | FAILED | ROLLED_BACK | INCOMPLETE
Feedback: OPEN → REVIEWED → RESOLVED; REVIEWED → OPEN when reopened
```

Invalid transitions return a conflict and do not mutate state. Duplicate
terminal reports replay the same outcome. New releases never inherit
certification; manual rollback suppression clears only for a strictly higher
semantic version.

## Index Strategy

- Search projection: publication/visibility/category plus normalized searchable
  fields; Oracle Text or another specialized index is not assumed for MVP.
- Skill detail: unique slug and Skill/version publication indexes.
- My Skills: actor, state, scope, project, and Skill composite indexes.
- Jobs: state + next-attempt/lease expiry; unique operation/idempotency indexes.
- Feedback: version + type + independence + state and SLA due timestamp.
- Audit: target, actor, action, occurred_at, and correlation indexes.

## H2 / Oracle Compatibility Matrix

| Concern | Portable rule |
|---|---|
| IDs | Generated in application; no identity/sequence dependency in domain |
| Boolean | Logical Boolean mapped to reviewed portable physical representation |
| Enum | Stable String values, never ordinal |
| Timestamp | UTC instant; no database local-time default |
| Large text | Logical CLOB mapping; no query/sort dependence |
| Pagination | Framework-generated portable pagination; stable tie-breaker |
| Case search | Normalized shadow/search fields; no vendor-specific collation assumption |
| Migration | Fresh and upgrade path run against both engines |

## Edge-Rule Trace

- Scope identity: Personal/no project is valid; Project/nonblank project is
  valid; Project/blank project is rejected.
- Version suppression: rolled-back 1.2 suppresses 1.2; 1.1 does not clear it;
  1.3 clears eligibility suppression.
- Feedback threshold: nine independent reports do not suspend; ten do; duplicate
  reporter/evidence does not increment pending final OQ-008 policy.

