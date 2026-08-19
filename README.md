# Atlas Marketplace

Atlas Marketplace is an internal registry for discovering, evaluating, and
managing Skills whose source remains in owner-managed GitHub repositories.
This repository implements the RPGLE pilot as a Java 21/Spring Boot registry
API and a Vue 3 browser application.

## Current implementation

- Search and filter the local pilot catalog, including AS400, IBM i, and iSeries aliases.
- View a published Skill and its available versions.
- Create and replay idempotent lifecycle operations through a clearly labelled
  local fake adapter. It never claims to have changed local files.
- Submit a repository registration into `PENDING_EXTERNAL_VALIDATION`.
- Submit version-bound ratings and reviews with strict request validation.
- Persist local data in H2 and provide Oracle-compatible Flyway migrations for dev.
- Expose health checks, safe API errors, correlation IDs, Basic authentication in
  local/test only, and deny-by-default security outside local/test.

Real enterprise SSO, GitHub validation/synchronization, artifact verification,
certification, and the trusted local installer are intentionally blocked until
their external contracts and credentials are approved. The accepted SDD chain
under `docs/01-requirements` through `docs/06-tasks` records those boundaries.

## Prerequisites

- Azul Zulu JDK **21.0.8** (`java -version` must report Java 21)
- Node.js **22 LTS** with Corepack
- No system Maven installation is required; Maven Wrapper is committed

## Start locally

Start the API from PowerShell:

```powershell
cd services/registry-api
$env:JAVA_HOME = 'C:\Program Files\Zulu\zulu-21'
.\mvnw.cmd spring-boot:run
```

On macOS/Linux, use `./mvnw spring-boot:run`. The API listens on
`http://localhost:8080`, stores H2 data under `services/registry-api/data`, and
uses the local-only credentials `atlas-local` / `local-only`.

In another terminal, start the web app:

```powershell
cd apps/marketplace-web
corepack enable
pnpm install --frozen-lockfile
pnpm dev
```

Open `http://localhost:5173`. The Vite development proxy adds the local-only
Basic credential when forwarding `/api` requests. Do not reuse that credential
or proxy configuration in any deployed environment.

## Verify and build

```powershell
cd services/registry-api
.\mvnw.cmd verify

cd ..\..\apps\marketplace-web
pnpm lint
pnpm typecheck
pnpm test
pnpm build
```

Backend output is written below `services/registry-api/target`; frontend output
is written to `apps/marketplace-web/dist`. CI runs the same checks using Zulu
21.0.8 and Node.js 22.

## Oracle dev environment

The `dev` Spring profile uses Oracle and never falls back to H2. Supply secrets
through the deployment platform, not committed files:

```powershell
$env:SPRING_PROFILES_ACTIVE = 'dev'
$env:ATLAS_DB_URL = 'jdbc:oracle:thin:@//oracle-host:1521/ATLASDEV'
$env:ATLAS_DB_USERNAME = 'atlas_app'
$env:ATLAS_DB_PASSWORD = '<from-secret-store>'
cd services/registry-api
.\mvnw.cmd spring-boot:run
```

Flyway selects the common migration set and Oracle-compatible placeholders.
The Oracle JDBC driver is runtime-scoped. Because the enterprise identity
contract is unresolved, the dev profile currently denies all business API
access rather than enabling an unsafe fallback login.

## Deployment outline

1. Run all verification commands and build the backend JAR and frontend assets.
2. Provision an Oracle schema and least-privilege application credentials.
3. Inject `SPRING_PROFILES_ACTIVE=dev` and the three `ATLAS_DB_*` variables.
4. Deploy `registry-api-0.1.0-SNAPSHOT.jar` behind TLS and an approved identity
   gateway. Do not expose it until the SSO adapter is implemented.
5. Serve `apps/marketplace-web/dist` from the approved static web tier and route
   `/api` to the registry API without the Vite local authorization header.
6. Deploy the trusted installer separately after its signed protocol, Skill-root
   authorization, locking, recovery, and audit requirements are implemented.

## Repository map

```text
apps/marketplace-web/       Vue 3 browser UI
services/registry-api/      Spring Boot registry API
packages/contracts/         Versioned cross-boundary contracts
docs/01-requirements/       Accepted requirements
docs/02-user-stories/       Stories and acceptance criteria
docs/03-spec/               Behavioral specification
docs/04-architecture/       Slice architecture and data model
docs/05-design/             Detailed design and API guide
docs/06-tasks/              Implementation and verification tasks
```

See `AGENTS.md`, `PROJECT_RULES.md`, and
`docs/00-context/rpgle-skill-pilot-traceability.md` before extending behavior.
