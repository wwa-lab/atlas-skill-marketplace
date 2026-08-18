# Scripts

Repository automation belongs here when it has an explicit owner, documented
inputs/outputs, safe defaults, tests where appropriate, and a referenced workflow.
Do not add scripts that execute untrusted Skill metadata as shell commands.

## `verify-sdd-skills.sh`

Owner: Atlas Marketplace maintainers.

This read-only check clones the authoritative Control Tower repository at the
branch recorded in `docs/00-context/sdd-skills.lock`, verifies that the branch
still points to the reviewed commit, and compares its `.claude/skills/` tree
with the project-local `.agents/skills/` mirror.

```sh
./scripts/verify-sdd-skills.sh
```

Inputs are the committed lock file, network access to the fixed GitHub
repository, and the local skill tree. The script prints `PASS` only when the
upstream commit and every mirrored file match. It never modifies the repository.
