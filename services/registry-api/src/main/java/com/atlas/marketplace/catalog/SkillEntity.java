package com.atlas.marketplace.catalog;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity @Table(name = "skill")
public class SkillEntity {
    @Id private UUID id;
    @Column(nullable=false, unique=true, length=100) private String slug;
    @Column(name="display_name", nullable=false, length=160) private String displayName;
    @Column(nullable=false, length=2000) private String description;
    @Column(name="owner_display_name", nullable=false, length=160) private String ownerDisplayName;
    @Column(name="repository_ref", nullable=false, length=512) private String repositoryRef;
    @Column(name="category_code", nullable=false, length=80) private String categoryCode;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Visibility visibility;
    @Enumerated(EnumType.STRING) @Column(name="publication_state", nullable=false, length=20) private PublicationState publicationState;
    @Column(name="curated_report", length=4000) private String curatedReport;
    @Column(name="rating_value", precision=3, scale=2) private BigDecimal ratingValue;
    @Column(name="rating_count", nullable=false) private int ratingCount;
    @Version @JdbcTypeCode(SqlTypes.NUMERIC) @Column(name="row_version", nullable=false) private long rowVersion;
    protected SkillEntity() {}
    public UUID getId(){return id;} public String getSlug(){return slug;} public String getDisplayName(){return displayName;}
    public String getDescription(){return description;} public String getOwnerDisplayName(){return ownerDisplayName;}
    public String getRepositoryRef(){return repositoryRef;} public String getCategoryCode(){return categoryCode;}
    public Visibility getVisibility(){return visibility;} public PublicationState getPublicationState(){return publicationState;}
    public String getCuratedReport(){return curatedReport;} public BigDecimal getRatingValue(){return ratingValue;} public int getRatingCount(){return ratingCount;}
    public enum Visibility { INTERNAL, RESTRICTED, HIDDEN }
    public enum PublicationState { DRAFT, PUBLISHED, DEPRECATED, ARCHIVED }
}
