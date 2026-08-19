# RPGLE Skill Marketplace Pilot — API Implementation Guide

- **Status**: Accepted for task planning
- **Date**: 2026-08-18
- **Version**: v1
- **Base path**: `/api/v1`
- **Backend**: Java 21 / Spring Boot
- **Authentication**: company session model pending OQ-002; local-only fixture adapter permitted

## Contract Conventions

- JSON fields use lower camel case; timestamps are ISO-8601 UTC instants.
- UUIDs are opaque strings. Semantic versions are strings validated before use.
- Collection responses use `items`, `page`, `size`, `totalItems`, and `totalPages`.
- Mutations return the updated resource or `202 Accepted` plus an operation
  resource. Lifecycle, sync, and webhook requests use `Idempotency-Key` or a
  provider delivery identifier.
- Unknown request fields are rejected for security-sensitive contracts; unknown
  response fields must be ignored by compatible v1 clients.

## Authentication and Roles

| Role | Capabilities |
|---|---|
| Consumer | catalog, allowed detail, own installations/jobs/feedback |
| Owner / Maintainer | Consumer plus owned registration, sync, publish, replies |
| AI SME | version certification actions |
| Administrator | taxonomy, exceptions, ownership, audit according to policy |

Every operation also performs object authorization. Restricted/Hidden resources
return 404 to unauthorized callers. An ordinary inaccessible Skill may return a
limited projection but never eligible versions, artifact data, or install actions.

## Error Response

```json
{
  "error": {
    "code": "SKILL_NOT_FOUND",
    "message": "The requested Skill is unavailable.",
    "correlationId": "01J5EXAMPLE7G4M8",
    "fieldErrors": []
  }
}
```

| Status | Stable code examples |
|---:|---|
| 400 | `INVALID_REQUEST`, `INVALID_PAGINATION` |
| 401 | `AUTHENTICATION_REQUIRED` |
| 403 | `OPERATION_FORBIDDEN`, `REPOSITORY_ACCESS_REQUIRED` |
| 404 | `SKILL_NOT_FOUND`, `OPERATION_NOT_FOUND` |
| 409 | `STALE_RESOURCE`, `INVALID_STATE_TRANSITION`, `IDEMPOTENCY_CONFLICT` |
| 422 | `METADATA_INVALID`, `VERSION_TAG_MISMATCH`, `HOST_UNSUPPORTED` |
| 429 | `RATE_LIMITED` |
| 503 | `DEPENDENCY_UNAVAILABLE`, `INSTALLER_UNAVAILABLE` |

## Catalog Endpoints

| Operation | Method | Endpoint | Auth |
|---|---|---|---|
| Search Skills | GET | `/skills?q=&category=&page=0&size=20` | employee |
| Skill detail | GET | `/skills/{slug}` | employee |

Example search response:

```json
{
  "items": [{
    "id": "74b1504c-7ec3-4ec0-a90a-2a8e9efdc224",
    "slug": "rpgle-program-analysis",
    "displayName": "RPGLE Program Analysis",
    "description": "Analyze RPGLE program structure and dependencies.",
    "ownerDisplayName": "Atlas Engineering",
    "category": {"code": "IBM_ISERIES", "label": "IBM iSeries"},
    "tags": ["RPGLE"],
    "latestPublishedVersion": "1.2.0",
    "certificationState": "CERTIFIED",
    "rating": {"state": "RATED", "value": 4.8, "count": 24},
    "access": "VISIBLE"
  }],
  "page": 0, "size": 20, "totalItems": 1, "totalPages": 1
}
```

Validation: `q` maximum 200 characters; category is a managed code/alias; page is
non-negative; size is 1–50. Results never disclose hidden rows. Limited results
omit versions, certification details, repository, report, and actions.

Detail includes safe metadata, curated report sections, published eligible
versions, certification evidence, access state, and allowed actions. Artifact
locator/checksum is not exposed by catalog endpoints.

## Installation and Lifecycle Endpoints

| Operation | Method | Endpoint |
|---|---|---|
| My Skills | GET | `/installations` |
| Create lifecycle operation | POST | `/installations/operations` |
| Read operation | GET | `/operations/{operationId}` |

Create request:

```json
{
  "operationType": "INSTALL",
  "skillId": "74b1504c-7ec3-4ec0-a90a-2a8e9efdc224",
  "installationId": null,
  "hostCode": "VSCODE_COPILOT_CHAT",
  "scope": {"type": "PERSONAL", "projectRef": null},
  "targetVersion": "1.2.0",
  "riskAcknowledgement": null
}
```

`operationType` is `INSTALL`, `UPDATE`, `MANUAL_ROLLBACK`, or `UNINSTALL`.
Project scope requires a normalized nonblank project reference; Personal rejects
one. Update/rollback/uninstall require an owned installation ID. Risk
acknowledgment is required only when the selected version has a warning state and
must bind Skill Version and scope.

Accepted response:

```json
{
  "operationId": "c78cab8f-b138-43a4-88a9-7c068503ab37",
  "state": "REQUESTED",
  "operationType": "INSTALL",
  "statusUrl": "/api/v1/operations/c78cab8f-b138-43a4-88a9-7c068503ab37",
  "installerReadiness": "UNKNOWN",
  "createdAt": "2026-08-18T08:00:00Z"
}
```

The same idempotency key with identical intent returns the same operation. The
same key with different intent returns `409 IDEMPOTENCY_CONFLICT`.

## Owner and Publication Endpoints

| Operation | Method | Endpoint |
|---|---|---|
| Register | POST | `/registrations` |
| Read validation | GET | `/registrations/{id}` |
| Confirm ownership | POST | `/registrations/{id}/confirmations` |
| Publish version | POST | `/skills/{skillId}/publications` |
| Manual sync | POST | `/skills/{skillId}/syncs` |
| GitHub webhook | POST | `/integrations/github/webhooks` |

Registration request:

```json
{
  "repositoryRef": "github.example/atlas/rpgle-analysis",
  "ownerRef": "team:atlas-engineering",
  "primaryMaintainerRef": "user:maintainer-id"
}
```

Publication request binds `gitTag`, `sourceRevision`, `metadataSchemaVersion`,
and an artifact manifest reference. The API verifies rather than trusts these
values. A successful publish creates a new `UNCERTIFIED` version. Publication
fails with 422 for invalid metadata/tag/artifact and 409 for existing or stale
version intent.

Webhook accepts the GitHub provider body only after signature, repository, event,
delivery-ID, replay, and schema validation. Raw payloads are not returned or
written to ordinary audit context.

## Feedback and Certification Endpoints

| Operation | Method | Endpoint |
|---|---|---|
| Submit feedback | POST | `/skills/{skillId}/feedback` |
| My feedback | GET | `/feedback?mine=true` |
| Owner reply | POST | `/feedback/{feedbackId}/replies` |
| Transition feedback | PATCH | `/feedback/{feedbackId}/state` |
| Record assessment | POST | `/skill-versions/{versionId}/certifications` |

```json
{
  "installationId": "f1deaa44-4ecf-4c76-abbe-880a78b13e35",
  "type": "MISSED_DEPENDENCY",
  "comment": "The report omitted the called program PAYR001.",
  "programOrRepositoryRef": "PAYR001"
}
```

The server derives Skill Version and scope from the caller-owned installation.
Comment length is 1–4000 and reply length is 1–4000. Uploads, source fields,
prompt fields, and arbitrary paths are absent and rejected as unknown fields.
Allowed transitions are `OPEN → REVIEWED → RESOLVED` and `REVIEWED → OPEN`.

## Session and Operational Endpoints

| Operation | Method | Endpoint | Notes |
|---|---|---|---|
| Current session | GET | `/session` | identity display, roles, CSRF metadata as appropriate |
| Health | GET | `/actuator/health` | exposure restricted by environment |

Admin audit search is deliberately excluded from the first executable vertical
slice until its authorization/pagination/export policy is designed. Audit is
still written for every protected command.

## Concurrency and State

- Mutating aggregate requests carry an `If-Match`/resource version where stale
  user edits are possible.
- Lifecycle and sync commands use idempotency keys.
- Installer result contracts must include operation ID, contract version, state,
  safe outcome code, and evidence binding; their transport/security fields remain
  blocked by OQ-003–OQ-006.
- Invalid transitions return 409 and leave state unchanged.

## Integration Dependencies and Blockers

| Integration | Required record before real adapter |
|---|---|
| SSO/GitHub authorization | OQ-002 ADR/contract |
| VS Code Host behavior | OQ-003 research and capability contract |
| Installer/browser handoff | OQ-004/OQ-005 ADR and operation contract |
| Artifact source | OQ-006 ADR and manifest schema |
| Certification evaluator | OQ-007 evaluation protocol |
| Feedback independence | OQ-008 domain policy |

Local deterministic fakes may implement these ports for UI/API development but
must advertise themselves as non-production and cannot satisfy pilot acceptance.

## Contract Verification

- OpenAPI schema lint and breaking-change check.
- Provider/consumer contract tests for web/API and API/installer.
- Unsupported contract version, unknown sensitive field, replay, duplicate,
  stale transition, hidden-resource, and redaction tests.
- H2 and Oracle persistence integration tests for every endpoint that mutates
  durable state.
