package com.atlas.marketplace.lifecycle;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity @Table(name="lifecycle_job")
public class LifecycleJobEntity {
    @Id private UUID id;
    @Column(name="idempotency_key", nullable=false, unique=true, length=160) private String idempotencyKey;
    @Column(name="actor_ref", nullable=false, length=200) private String actorRef;
    @Column(name="installation_id") private UUID installationId;
    @Column(name="skill_id", nullable=false) private UUID skillId;
    @Enumerated(EnumType.STRING) @Column(name="operation_type", nullable=false, length=30) private OperationType operationType;
    @Column(name="target_version", length=64) private String targetVersion;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private JobState state;
    @Column(name="safe_message", length=500) private String safeMessage;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @Version @JdbcTypeCode(SqlTypes.NUMERIC) @Column(name="row_version", nullable=false) private long rowVersion;
    protected LifecycleJobEntity() {}
    LifecycleJobEntity(String key,String actor,UUID installationId,UUID skillId,OperationType type,String target){
        this.id=UUID.randomUUID();this.idempotencyKey=key;this.actorRef=actor;this.installationId=installationId;this.skillId=skillId;
        this.operationType=type;this.targetVersion=target;this.state=JobState.SUCCEEDED;this.safeMessage="Completed by the local deterministic adapter; no local files were changed.";
        this.createdAt=Instant.now();this.updatedAt=this.createdAt;
    }
    public UUID getId(){return id;} public String getIdempotencyKey(){return idempotencyKey;} public UUID getSkillId(){return skillId;}
    public OperationType getOperationType(){return operationType;} public String getTargetVersion(){return targetVersion;}
    public JobState getState(){return state;} public String getSafeMessage(){return safeMessage;} public Instant getCreatedAt(){return createdAt;}
    public enum OperationType { INSTALL, UPDATE, MANUAL_ROLLBACK, UNINSTALL }
    public enum JobState { REQUESTED, READY, DISPATCHED, RUNNING, SUCCEEDED, FAILED, ROLLED_BACK, INCOMPLETE }
}
