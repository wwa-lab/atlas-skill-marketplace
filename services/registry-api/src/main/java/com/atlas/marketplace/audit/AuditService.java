package com.atlas.marketplace.audit;

import com.atlas.marketplace.shared.CorrelationFilter;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class AuditService {
    private final AuditEventRepository events;

    AuditService(AuditEventRepository events) {
        this.events = events;
    }

    public void record(String actor, String action, String resourceType, Object resourceId, String outcome) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        Object value = attributes == null ? null : attributes.getAttribute(CorrelationFilter.ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        String correlationId = value == null ? UUID.randomUUID().toString() : value.toString();
        events.save(new AuditEventEntity(actor, action, resourceType, String.valueOf(resourceId), outcome, correlationId));
    }
}
