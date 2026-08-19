CREATE TABLE registration (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  repository_ref VARCHAR(512) NOT NULL,
  owner_ref VARCHAR(200) NOT NULL,
  primary_maintainer_ref VARCHAR(200) NOT NULL,
  requester_ref VARCHAR(200) NOT NULL,
  state VARCHAR(30) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  row_version ${bigint_type} NOT NULL
);
CREATE TABLE feedback_reply (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  feedback_id VARCHAR(36) NOT NULL,
  author_ref VARCHAR(200) NOT NULL,
  reply_text VARCHAR(4000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  CONSTRAINT fk_reply_feedback FOREIGN KEY (feedback_id) REFERENCES feedback(id)
);
CREATE INDEX ix_registration_requester ON registration(requester_ref, state);
CREATE INDEX ix_reply_feedback ON feedback_reply(feedback_id, created_at);
