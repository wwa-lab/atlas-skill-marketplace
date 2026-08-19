package com.atlas.marketplace.owner;
import com.atlas.marketplace.audit.AuditService;
import com.atlas.marketplace.shared.ApiException;

import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.Size;import java.security.Principal;import java.time.Instant;import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/registrations")
public class RegistrationController {
 private final RegistrationRepository registrations; private final AuditService audit;
 RegistrationController(RegistrationRepository registrations,AuditService audit){this.registrations=registrations;this.audit=audit;}
 @PostMapping @Transactional RegistrationView create(@Valid @RequestBody RegistrationRequest request,Principal principal){var saved=registrations.save(new RegistrationEntity(request.repositoryRef(),request.ownerRef(),request.primaryMaintainerRef(),principal.getName()));audit.record(principal.getName(),"REGISTRATION_CREATED","REGISTRATION",saved.getId(),saved.getState());return view(saved);}
 @GetMapping("/{id}") RegistrationView get(@PathVariable UUID id){return view(registrations.findById(id).orElseThrow(()->new ApiException(org.springframework.http.HttpStatus.NOT_FOUND,"REGISTRATION_NOT_FOUND","The requested registration is unavailable.")));}
 private RegistrationView view(RegistrationEntity r){return new RegistrationView(r.getId(),r.getRepositoryRef(),r.getOwnerRef(),r.getPrimaryMaintainerRef(),r.getState(),r.getCreatedAt());}
 record RegistrationRequest(@NotBlank @Size(max=512) String repositoryRef,@NotBlank @Size(max=200) String ownerRef,@NotBlank @Size(max=200) String primaryMaintainerRef){}
 record RegistrationView(UUID id,String repositoryRef,String ownerRef,String primaryMaintainerRef,String state,Instant createdAt){}
}
