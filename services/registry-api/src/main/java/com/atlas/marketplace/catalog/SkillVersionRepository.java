package com.atlas.marketplace.catalog;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillVersionRepository extends JpaRepository<SkillVersionEntity, UUID> {
    List<SkillVersionEntity> findBySkillIdAndPublicationStateOrderByPublishedAtDesc(UUID skillId, SkillEntity.PublicationState state);
    boolean existsByIdAndSkillId(UUID id, UUID skillId);
}
