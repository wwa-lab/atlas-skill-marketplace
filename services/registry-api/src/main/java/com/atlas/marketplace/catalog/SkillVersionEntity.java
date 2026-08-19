package com.atlas.marketplace.catalog;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity @Table(name="skill_version")
public class SkillVersionEntity {
    @Id private UUID id;
    @Column(name="skill_id", nullable=false) private UUID skillId;
    @Column(name="git_tag", nullable=false, length=120) private String gitTag;
    @Column(name="source_revision", nullable=false, length=128) private String sourceRevision;
    @Column(name="semantic_version", nullable=false, length=64) private String semanticVersion;
    @Column(nullable=false, length=4000) private String changelog;
    @Enumerated(EnumType.STRING) @Column(name="publication_state", nullable=false, length=20) private SkillEntity.PublicationState publicationState;
    @Enumerated(EnumType.STRING) @Column(name="certification_state", nullable=false, length=20) private CertificationState certificationState;
    @Column(name="published_at") private Instant publishedAt;
    @Version @JdbcTypeCode(SqlTypes.NUMERIC) @Column(name="row_version", nullable=false) private long rowVersion;
    protected SkillVersionEntity() {}
    public UUID getId(){return id;} public UUID getSkillId(){return skillId;} public String getGitTag(){return gitTag;}
    public String getSemanticVersion(){return semanticVersion;} public String getChangelog(){return changelog;}
    public CertificationState getCertificationState(){return certificationState;} public Instant getPublishedAt(){return publishedAt;}
    public enum CertificationState { UNCERTIFIED, CERTIFIED, SUSPENDED, FAILED }
}
