# Host Adapters

Canonical boundary for GitHub Copilot, OpenCode, and future approved host
capabilities. Each adapter defines canonical roots, preflight, activation,
validation, enable/disable, uninstall, and supported one-click operations.

An adapter must fail closed for unsupported capabilities and must never accept an
unrestricted path or command from Skill metadata.
