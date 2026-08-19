package com.atlas.marketplace.lifecycle;

import com.atlas.marketplace.audit.AuditService;
import com.atlas.marketplace.shared.ApiException;
import java.security.Principal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifecycleService {
    private final LifecycleJobRepository jobs;
    private final AuditService audit;
    LifecycleService(LifecycleJobRepository jobs, AuditService audit){this.jobs=jobs;this.audit=audit;}
    @Transactional public OperationView create(String key, CreateOperation request, Principal principal){
        if(key==null||!key.matches("[A-Za-z0-9._-]{8,160}")) throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_REQUEST","A valid Idempotency-Key is required.");
        if(request.scope()==null || (request.scope().type()==ScopeType.PROJECT && (request.scope().projectRef()==null||request.scope().projectRef().isBlank()))
            || (request.scope().type()==ScopeType.PERSONAL && request.scope().projectRef()!=null))
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,"INVALID_SCOPE","The installation scope is invalid.");
        var existing=jobs.findByIdempotencyKey(key);
        if(existing.isPresent()){
            var job=existing.get();
            if(!job.getSkillId().equals(request.skillId())||job.getOperationType()!=request.operationType()||!java.util.Objects.equals(job.getTargetVersion(),request.targetVersion()))
                throw new ApiException(HttpStatus.CONFLICT,"IDEMPOTENCY_CONFLICT","The idempotency key is already bound to different intent.");
            return view(job);
        }
        var saved=jobs.save(new LifecycleJobEntity(key,principal.getName(),request.installationId(),request.skillId(),request.operationType(),request.targetVersion()));
        audit.record(principal.getName(), "LIFECYCLE_"+request.operationType(), "LIFECYCLE_JOB", saved.getId(), saved.getState().name());
        return view(saved);
    }
    @Transactional(readOnly=true) public OperationView get(UUID id){return view(jobs.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"OPERATION_NOT_FOUND","The requested operation is unavailable.")));}
    private OperationView view(LifecycleJobEntity j){return new OperationView(j.getId(),j.getState().name(),j.getOperationType().name(),"/api/v1/operations/"+j.getId(),"LOCAL_FAKE_READY",j.getSafeMessage(),j.getCreatedAt());}
    public record CreateOperation(LifecycleJobEntity.OperationType operationType,UUID skillId,UUID installationId,String hostCode,Scope scope,String targetVersion,String riskAcknowledgement){}
    public record Scope(ScopeType type,String projectRef){} public enum ScopeType { PERSONAL, PROJECT }
    public record OperationView(UUID operationId,String state,String operationType,String statusUrl,String installerReadiness,String message,Instant createdAt){}
}
