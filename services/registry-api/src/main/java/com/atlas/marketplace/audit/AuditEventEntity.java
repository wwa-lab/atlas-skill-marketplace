package com.atlas.marketplace.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_event")
class AuditEventEntity {
    @Id
    @Column(name = "id")
    private UUID eventId;
    @Column(name = "actor_ref", nullable = false)
    private String actorRef;
    @Column(name = "action_code", nullable = false)
    private String action;
    @Column(name = "target_type", nullable = false)
    private String resourceType;
    @Column(name = "target_id", nullable = false)
    private String resourceId;
    @Column(nullable = false)
    private String outcome;
    @Column(name = "correlation_id", nullable = false)
    private String correlationId;
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected AuditEventEntity() {}

    AuditEventEntity(String actorRef, String action, String resourceType, String resourceId, String outcome, String correlationId) {
        this.eventId = UUID.randomUUID();
        this.actorRef = actorRef;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.outcome = outcome;
        this.correlationId = correlationId;
        this.occurredAt = Instant.now();
    }
}
