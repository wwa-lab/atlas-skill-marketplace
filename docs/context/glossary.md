# Glossary

## Roles

- **Consumer**: discovers, installs, updates, rolls back, uninstalls, rates, and
  provides feedback on Skills.
- **Owner / Maintainer**: registers and maintains Skill metadata, versions,
  lifecycle, sync, and feedback.
- **Admin**: governs access, taxonomy, Featured placement, verification, and
  marketplace lifecycle.

## Core Terms

- **Skill**: the registry identity and product presentation of an AI Skill.
- **Skill Version**: one publishable version with changelog, compatibility, and
  lifecycle instructions.
- **Registry**: centralized metadata and lifecycle state; not a source-code host.
- **Repository**: the owner-controlled GitHub source and access boundary.
- **Distribution Artifact**: an immutable installable package traceable to a
  repository revision and verified before use.
- **Installation**: one user's Skill state in one host/environment.
- **Local Installer**: trusted local component that performs constrained file
  operations and reports lifecycle progress/results.
- **Host Adapter**: host-specific install paths, capability checks, validation,
  activation, and removal behavior.
- **Update Job**: durable state machine for a lifecycle operation.
- **Backup**: a retained prior installation that can be automatically or manually
  restored.
- **Certification**: an IBM iSeries SME's evidence-backed conclusion for exactly
  one Skill Version after running the standard RPGLE test set.
- **Installation Scope**: either Personal or one identified Project. Project scope
  takes precedence while the Host operates in that Project.

## Version Vocabulary

- **Repository Metadata Version**: version declared by the source repository.
- **Published Version**: version accepted and visible in Marketplace.
- **Latest Version**: newest version eligible for the current user/host.
- **Installed Version**: version currently active in a particular installation.
- **Target Version**: version requested by an update or rollback job.
- **Previous Version**: prior active version associated with a restorable backup.

Never collapse these into one ambiguous `version` field across boundaries.

## Fixed Product Vocabulary

SDLC phase order:

`Planning -> Estimation -> Discovery -> Build -> Testing -> Deployment -> Maintenance`

Lifecycle states:

- Validation: `Error`, `Warning`, `Passed`
- Skill/version: `Draft`, `Published`, `Deprecated`, `Archived`
- Installation: `Installed`, `Enabled`, `Disabled`, `Update Available`,
  `Install Incomplete`, `Uninstalled`
- Update job: `Pending`, `Downloading`, `Backing Up`, `Installing`, `Validating`,
  `Completed`, `Rolled Back`, `Failed`
- Feedback: `Open`, `Reviewed`, `Resolved`
- Certification: `Uncertified`, `Certified`, `Suspended`, `Failed`
