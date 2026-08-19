package com.atlas.marketplace.feedback;

import com.atlas.marketplace.audit.AuditService;
import com.atlas.marketplace.catalog.SkillVersionRepository;
import com.atlas.marketplace.shared.ApiException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FeedbackController {
    private final FeedbackRepository feedback;
    private final SkillVersionRepository versions;
    private final AuditService audit;

    FeedbackController(FeedbackRepository feedback, SkillVersionRepository versions, AuditService audit) {
        this.feedback = feedback;
        this.versions = versions;
        this.audit = audit;
    }

    @PostMapping("/skills/{skillId}/feedback")
    @Transactional
    FeedbackView create(@PathVariable UUID skillId, @Valid @RequestBody FeedbackRequest request, Principal principal) {
        if (!versions.existsByIdAndSkillId(request.skillVersionId(), skillId)) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_SKILL_VERSION",
                    "The version is not published for this Skill.");
        }
        var saved = feedback.save(new FeedbackEntity(principal.getName(), skillId, request.skillVersionId(),
                request.installationId(), request.type(), request.comment(), request.programOrRepositoryRef()));
        audit.record(principal.getName(), "FEEDBACK_SUBMITTED", "FEEDBACK", saved.getId(), saved.getState());
        return view(saved);
    }

    @GetMapping("/feedback")
    List<FeedbackView> mine(Principal principal) {
        return feedback.findByActorRefOrderByCreatedAtDesc(principal.getName()).stream().map(this::view).toList();
    }

    private FeedbackView view(FeedbackEntity value) {
        return new FeedbackView(value.getId(), value.getSkillVersionId(), value.getFeedbackType(),
                value.getCommentText(), value.getState(), value.getCreatedAt());
    }

    record FeedbackRequest(@NotNull UUID skillVersionId, UUID installationId,
                           @NotBlank @Size(max = 40) String type,
                           @NotBlank @Size(max = 4000) String comment,
                           @Size(max = 300) String programOrRepositoryRef) {}

    record FeedbackView(UUID id, UUID skillVersionId, String type, String comment, String state, Instant createdAt) {}
}
