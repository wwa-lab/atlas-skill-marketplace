package com.atlas.marketplace.owner;

import jakarta.persistence.*;import java.time.Instant;import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;import org.hibernate.type.SqlTypes;
@Entity @Table(name="registration")
public class RegistrationEntity {
 @Id private UUID id; @Column(name="repository_ref",nullable=false,length=512) private String repositoryRef;
 @Column(name="owner_ref",nullable=false,length=200) private String ownerRef; @Column(name="primary_maintainer_ref",nullable=false,length=200) private String primaryMaintainerRef;
 @Column(name="requester_ref",nullable=false,length=200) private String requesterRef; @Column(nullable=false,length=30) private String state;
 @Column(name="created_at",nullable=false) private Instant createdAt; @Version @JdbcTypeCode(SqlTypes.NUMERIC) @Column(name="row_version",nullable=false) private long rowVersion;
 protected RegistrationEntity(){} RegistrationEntity(String repo,String owner,String maintainer,String requester){id=UUID.randomUUID();repositoryRef=repo;ownerRef=owner;primaryMaintainerRef=maintainer;requesterRef=requester;state="PENDING_EXTERNAL_VALIDATION";createdAt=Instant.now();}
 public UUID getId(){return id;} public String getRepositoryRef(){return repositoryRef;} public String getOwnerRef(){return ownerRef;} public String getPrimaryMaintainerRef(){return primaryMaintainerRef;} public String getState(){return state;} public Instant getCreatedAt(){return createdAt;}
}
