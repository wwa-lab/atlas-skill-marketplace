# Atlas Installer Standard

The local installer is a privileged component. Its implementation language and
transport are unresolved until ADR approval; its safety contract is not.

## Operation Model

- Accept structured, versioned lifecycle requests; never arbitrary shell text.
- Require an operation id, installation id, target host, source/target versions,
  immutable artifact identity, expected integrity, and explicit user consent.
- Resolve all paths through the selected host adapter and an allowlisted root.
- Journal every state transition needed for safe restart and recovery.
- Lock per installation. A duplicate operation id returns/replays the durable
  result rather than running the operation twice.

## Update Sequence

1. preflight path, permission, host capability, disk space, and current version;
2. download to a controlled staging area;
3. verify manifest, provenance, integrity, compatibility, structure, and limits;
4. prepare a recoverable backup record;
5. atomically rename/switch the current installation where supported;
6. activate the verified target at the canonical path;
7. run host-specific post-install validation;
8. report completion and retain/clean backup according to policy;
9. on failure after activation begins, quarantine the target and restore/validate
   the prior version before reporting the final outcome.

## Filesystem Safety

- Resolve canonical paths immediately before mutation and prove containment.
- Reject absolute archive entries, traversal, unsafe separators, devices, pipes,
  sockets, hard links, and symlinks unless a narrowly reviewed policy permits
  them.
- Enforce archive size, expansion ratio, entry count, nesting, and file type
  limits.
- Use narrowly scoped filesystem APIs and argument arrays; do not interpolate
  metadata into a shell.
- Deletion and cleanup operate only on job-owned, verified paths.

## Host Adapters

Adapters declare supported capabilities and implement path resolution, preflight,
activation validation, enable/disable, uninstall, and health checks. Unsupported
capabilities fail closed and must not be advertised as one-click.

## Verification Baseline

Installer changes require unit and contract tests plus temporary-directory
integration tests for success, every rollback point, cancellation, interruption,
restart, retry, concurrency, hostile archives/paths, integrity mismatch, and
permission/disk failures. Run platform-specific tests for each supported host.
