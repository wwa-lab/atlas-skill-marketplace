# ADR-0004: Adopt Java, Spring Boot, Vue, and Relational Persistence

- **Status**: Accepted
- **Date**: 2026-08-18
- **Owners**: Atlas Marketplace engineering
- **Related Spec**: `docs/03-spec/rpgle-skill-pilot-spec.md`

## Context

The RPGLE pilot needs an implementation stack for the Marketplace Web and
Registry API. The repository currently contains no runtime scaffolding. The
selected stack must support local development without shared infrastructure and
deployment to an environment that uses Oracle, while preserving the browser,
API, GitHub, artifact, and local-installer trust boundaries.

## Decision Drivers

- [USER-STATED] Use Azul Zulu OpenJDK 21.0.8 and Spring Boot for the API.
- [USER-STATED] Use Vue 3 for the web application.
- [USER-STATED] Use H2 locally and Oracle in the dev environment.
- Keep domain and application code independent of database and transport details.
- Detect H2/Oracle incompatibilities before deployment rather than relying on H2
  as proof of Oracle compatibility.
- Start with the minimum number of deployable units required by the system
  overview.

## Considered Options

1. Java 21/Spring Boot API, Vue 3 web, H2 local, Oracle dev.
2. A JavaScript/TypeScript full-stack runtime.
3. Java/Spring Boot with Oracle for every environment.

## Decision

Use Azul Zulu OpenJDK 21.0.8 as the supported JDK distribution and Java 21 as
the language/runtime level. Implement the Registry API as a Spring Boot modular
monolith with Maven, Spring Web, Bean Validation, Spring Security, Spring Data
JPA, Flyway, and Actuator. Use an OpenAPI-described REST/JSON boundary.

Implement Marketplace Web as a Vue 3 application using TypeScript, Vite, Vue
Router, Pinia, and a generated or contract-aligned API client. Use Node.js 22 LTS
and pnpm through Corepack with a committed lockfile for reproducible frontend builds.

Use H2 only for the `local` Spring profile and Oracle for the `dev` profile.
Flyway owns schema evolution. Migrations use the portable SQL subset supported
by both databases. Database-generated UUID expressions, H2 compatibility modes,
quoted case-sensitive identifiers, vendor-only column types, and native queries
are prohibited unless isolated in a reviewed adapter or vendor-specific
migration. IDs are generated in application code as UUID values. Boolean domain
values use numeric `0/1` persistence where a physical representation is needed;
large text uses JPA logical mappings with database-specific DDL isolated by
Flyway location only when portable DDL is insufficient.

H2 tests provide fast local feedback, but Oracle compatibility is established
only by running the same migration and persistence integration suite against an
approved Oracle test instance. A passing H2 suite is not an Oracle sign-off.

The web app and API are separate build artifacts and deployable units. The local
installer remains a third deployable trust boundary; its runtime and protocol
are not selected by this ADR.

## Consequences

### Positive

- Local development can run without a shared database.
- The API uses a supported long-term Java runtime and conventional Spring
  layering.
- Database access remains replaceable and contract tested.
- Vue components remain separate from server authorization and persistence.

### Negative / Trade-offs

- H2 cannot reproduce all Oracle behavior, so an Oracle-backed verification
  environment is mandatory before dev deployment.
- Two frontend/backend toolchains and lockfiles must be maintained.
- Portable migrations may avoid useful Oracle-specific features until justified.
- Installer, SSO, GitHub, and artifact decisions still require separate ADRs.

## Security And Privacy Impact

Spring Security enforces authentication, role checks, CSRF/session policy, and
object authorization at the API boundary. Vue route guards are presentation
controls only. Credentials are injected through environment configuration and
must not be committed or stored in browser persistence. Actuator endpoints are
restricted and must not expose environment secrets. Persistence and structured
logs exclude source code, prompts, tokens, artifact contents, and unrestricted
local paths.

## Compatibility And Migration

The initial schema starts at Flyway version 1. Every migration is forward-only
in deployed environments and must be verified on a fresh H2 database and the
supported Oracle dev version. The exact Oracle server version and driver remain
deployment configuration that must be recorded before the first dev release.
API compatibility is versioned under `/api/v1`; persistence entities are not API
contracts.

## Verification

- Confirm `java -version` reports Zulu 21.0.8 and Maven compiles with release 21.
- Run backend unit and H2 integration tests plus Flyway clean migration tests.
- Run the same migration and persistence contract tests against approved Oracle.
- Run frontend lint, typecheck, unit/component tests, accessibility checks, and
  production build.
- Verify no H2-only SQL or native database behavior is used in domain paths.
