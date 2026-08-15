# Security Boundaries

## Trust Zones

1. **Browser**: untrusted display/input environment; no secret repository token
   or unrestricted local capability.
2. **Registry API**: trusted business-policy boundary; validates inputs and
   enforces RBAC, but cannot assume GitHub or local filesystem authority.
3. **GitHub and artifact sources**: external inputs even when internal; payloads,
   metadata, archives, tags, and webhooks are untrusted until verified.
4. **Local installer**: high-impact boundary with narrowly scoped filesystem
   permission; every operation requires canonical target and structured intent.
5. **Host install roots**: allowlisted local roots only; unrelated user data is
   out of bounds.

## Required Controls

### Metadata And Web

- schema-validate metadata and repository payloads
- sanitize Markdown/HTML before rendering
- use link and URI allowlists where data controls navigation
- never interpolate metadata into inline event handlers or raw `innerHTML`
- apply CSRF, XSS, session, and authorization controls appropriate to the chosen
  stack; frontend guards are not authorization

### Artifact Supply Chain

- resolve immutable artifact identity before download
- bind artifact to repository, commit/tag, semantic version, host, and manifest
- verify checksum or signature before extraction and again before activation
- apply archive entry, size, file-count, nested archive, traversal, and symlink
  limits; reject on ambiguity
- do not log tokens, signed URLs, source content, or archive contents

### Local Filesystem Lifecycle

- canonicalize both root and target and verify containment immediately before
  each destructive step
- avoid unrestricted shell evaluation; use structured adapter operations
- stage on a filesystem compatible with the selected atomic switch strategy
- lock per installation and make job identity/retry semantics explicit
- maintain a journal sufficient to recover after process or machine interruption
- quarantine incomplete targets and restore a verified backup on activation or
  post-install failure
- make uninstall recoverable where practical and never follow untrusted symlinks

### Audit And Privacy

Audit lifecycle and management actions with actor, action, target identifiers,
versions, host, job id, outcome, and timestamps. Redact secrets, raw source,
prompt content, unrestricted local paths, and sensitive metadata. Analytics may
measure Marketplace behavior and lifecycle outcomes but not code or prompts.

## Required Security Test Classes

- authorization matrix and object-level access
- command/argument injection
- path traversal, Zip Slip, absolute paths, alternate separators, and symlinks
- archive bombs, oversized files, and malformed manifests
- checksum/signature failure and source/version mismatch
- duplicate, concurrent, retried, cancelled, and interrupted jobs
- rollback after each activation step and recovery after client restart
- metadata XSS and unsafe link schemes
