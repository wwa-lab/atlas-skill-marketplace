CREATE TABLE skill (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  slug VARCHAR(100) NOT NULL UNIQUE,
  display_name VARCHAR(160) NOT NULL,
  description VARCHAR(2000) NOT NULL,
  owner_display_name VARCHAR(160) NOT NULL,
  repository_ref VARCHAR(512) NOT NULL,
  category_code VARCHAR(80) NOT NULL,
  visibility VARCHAR(20) NOT NULL,
  publication_state VARCHAR(20) NOT NULL,
  curated_report VARCHAR(4000),
  rating_value DECIMAL(3,2),
  rating_count INTEGER NOT NULL,
  row_version ${bigint_type} NOT NULL
);
CREATE TABLE skill_version (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  skill_id VARCHAR(36) NOT NULL,
  git_tag VARCHAR(120) NOT NULL,
  source_revision VARCHAR(128) NOT NULL,
  semantic_version VARCHAR(64) NOT NULL,
  changelog VARCHAR(4000) NOT NULL,
  publication_state VARCHAR(20) NOT NULL,
  certification_state VARCHAR(20) NOT NULL,
  published_at TIMESTAMP,
  row_version ${bigint_type} NOT NULL,
  CONSTRAINT fk_version_skill FOREIGN KEY (skill_id) REFERENCES skill(id),
  CONSTRAINT uq_skill_git_tag UNIQUE (skill_id, git_tag),
  CONSTRAINT uq_skill_semver UNIQUE (skill_id, semantic_version)
);
CREATE TABLE installation (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  actor_ref VARCHAR(200) NOT NULL,
  skill_id VARCHAR(36) NOT NULL,
  host_code VARCHAR(80) NOT NULL,
  scope_type VARCHAR(20) NOT NULL,
  project_ref VARCHAR(300),
  installed_version_id VARCHAR(36),
  latest_eligible_version_id VARCHAR(36),
  previous_version_id VARCHAR(36),
  suppressed_version_id VARCHAR(36),
  state VARCHAR(30) NOT NULL,
  last_reconciled_at TIMESTAMP,
  row_version ${bigint_type} NOT NULL,
  CONSTRAINT fk_install_skill FOREIGN KEY (skill_id) REFERENCES skill(id)
);
CREATE TABLE lifecycle_job (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  idempotency_key VARCHAR(160) NOT NULL UNIQUE,
  actor_ref VARCHAR(200) NOT NULL,
  installation_id VARCHAR(36),
  skill_id VARCHAR(36) NOT NULL,
  operation_type VARCHAR(30) NOT NULL,
  target_version VARCHAR(64),
  state VARCHAR(30) NOT NULL,
  safe_message VARCHAR(500),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  row_version ${bigint_type} NOT NULL
);
CREATE TABLE feedback (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  actor_ref VARCHAR(200) NOT NULL,
  skill_id VARCHAR(36) NOT NULL,
  skill_version_id VARCHAR(36) NOT NULL,
  installation_id VARCHAR(36),
  feedback_type VARCHAR(40) NOT NULL,
  comment_text VARCHAR(4000) NOT NULL,
  program_ref VARCHAR(300),
  state VARCHAR(20) NOT NULL,
  independent_flag INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  row_version ${bigint_type} NOT NULL
);
CREATE TABLE audit_event (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  occurred_at TIMESTAMP NOT NULL,
  actor_ref VARCHAR(200) NOT NULL,
  action_code VARCHAR(100) NOT NULL,
  target_type VARCHAR(80) NOT NULL,
  target_id VARCHAR(36) NOT NULL,
  outcome VARCHAR(30) NOT NULL,
  correlation_id VARCHAR(100) NOT NULL,
  safe_context VARCHAR(1000)
);
CREATE INDEX ix_skill_catalog ON skill(publication_state, visibility, category_code);
CREATE INDEX ix_install_actor ON installation(actor_ref, state, skill_id);
CREATE INDEX ix_job_state ON lifecycle_job(state, updated_at);
CREATE INDEX ix_feedback_version ON feedback(skill_version_id, feedback_type, state);
