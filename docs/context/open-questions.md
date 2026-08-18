# Open Questions

Product behavior for the first RPGLE pilot is defined in PRD v0.3 and
`docs/03-spec/rpgle-skill-pilot-spec.md`. The questions below block technical planning
or implementation, not product discovery. Do not turn an unresolved option into an
implicit architecture decision.

## Pilot Architecture And Evidence

| ID | Decision / Evidence Needed | Why It Blocks Work | Expected Record |
|---|---|---|---|
| OQ-001 | Web/API stack and deployment topology | Determines runtime scaffolding and operations | ADR |
| OQ-002 | GitHub Enterprise type, API surface, and company SSO mapping | Determines login, Repository permissions, and Owner verification | ADR |
| OQ-003 | Officially supported VS Code GitHub Copilot Chat Skill root, loading/refresh behavior, precedence, and explicit invocation syntax | File copy and Copilot CLI documentation alone cannot prove pilot Host success | Research + host contract |
| OQ-004 | Trusted Windows component form: CLI, service, VS Code extension, or constrained combination | Determines consent, update, filesystem, and deployment boundaries | ADR |
| OQ-005 | Browser-to-local discovery, consent, resume, expiry, duplicate delivery, and result-reconciliation protocol | Determines whether one-click install can be secure and recoverable | ADR + contract |
| OQ-006 | Distribution Artifact builder, custody, format, checksum/signature, retention, revocation, and authorization | Determines supply-chain trust | ADR + manifest schema |
| OQ-007 | Standard RPGLE program set, dependency truth set, scoring rule, and valid-output criteria | Determines certification and the pilot success endpoint | Evaluation protocol |
| OQ-008 | Independent missed-dependency feedback deduplication and anti-abuse rule | Determines whether the certification-suspension threshold of ten is meaningful | Product rule + domain contract |
| OQ-009 | Restricted/Hidden Metadata indexing, caching, and direct-URL behavior | Prevents permission leakage through derived surfaces | ADR + authorization tests |
| OQ-010 | User-confirmed local-component self-update signature, compatibility, and recovery mechanism | Determines whether Skill lifecycle remains maintainable after pilot rollout | ADR + client contract |

## Confirmed Decisions — Do Not Reopen Implicitly

- The pilot OS is Windows and the primary Host is VS Code GitHub Copilot Chat.
- The browser and Registry API never perform arbitrary local shell or file actions.
- Git Tag is the Published Version authority. If Repository Metadata also declares
  a version, it must match the Tag.
- Repository Metadata is the primary descriptive source; Atlas only fills gaps.
- Repository synchronization uses webhook plus manual Refresh.
- Personal and Project scopes are separate installations; Project wins in that
  Project and each scope updates independently.
- Only the immediately previous local version is retained for rollback.
- Skill updates and local-component updates require user confirmation.
- New Skill Versions begin Uncertified; certification is version-specific.
- Uncertified, Suspended, or overdue-high-risk versions remain installable after an
  obvious warning.
- Certification is suspended after ten independent missed-dependency reports for
  that version over its lifecycle.
- Ordinary internal Skill name/description may be visible without Repository access;
  Restricted/Hidden Metadata inherits Repository visibility and remains hidden.
- Day 2 surfaces may say `Coming Soon` but may not masquerade fixture data as live.

## Evidence Note: Copilot Invocation

Official GitHub documentation describes explicit `/SKILL-NAME` invocation for
Copilot CLI. That evidence must not be generalized to VS Code GitHub Copilot Chat
without official Host documentation or a repeatable Windows pilot test. The final
copyable command in Atlas depends on OQ-003.
