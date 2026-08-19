package com.atlas.marketplace.catalog;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface SkillRepository extends JpaRepository<SkillEntity, UUID> {
    Page<SkillEntity> findByPublicationStateAndDisplayNameContainingIgnoreCase(SkillEntity.PublicationState state, String query, Pageable pageable);
    Page<SkillEntity> findByPublicationStateAndCategoryCode(SkillEntity.PublicationState state, String categoryCode, Pageable pageable);
    Page<SkillEntity> findByPublicationState(SkillEntity.PublicationState state, Pageable pageable);
    Optional<SkillEntity> findBySlugAndPublicationState(String slug, SkillEntity.PublicationState state);
}
