# Contracts

Canonical home for versioned cross-boundary schemas, generated types, fixtures,
and compatibility tests, including:

- Skill metadata schema
- Registry API schema
- local installer request/progress/result protocol
- distribution artifact manifest
- GitHub sync/webhook payload normalization
- host capability declarations

Choose schema languages and code generation only after stack ADRs. Contracts must
not depend on UI components, transport framework types, or persistence models.
