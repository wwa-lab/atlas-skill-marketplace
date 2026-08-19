package com.atlas.marketplace.catalog;

import com.atlas.marketplace.shared.ApiException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly=true)
public class CatalogService {
    private final SkillRepository skills; private final SkillVersionRepository versions;
    CatalogService(SkillRepository skills, SkillVersionRepository versions){this.skills=skills;this.versions=versions;}
    public SearchResponse search(String rawQuery, String rawCategory, int page, int size) {
        String query = normalize(rawQuery); String category = canonicalCategory(rawCategory);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("ratingValue"), Sort.Order.asc("displayName"), Sort.Order.asc("id")));
        var result = !category.isBlank() ? skills.findByPublicationStateAndCategoryCode(SkillEntity.PublicationState.PUBLISHED, category, pageable)
            : !query.isBlank() ? skills.findByPublicationStateAndDisplayNameContainingIgnoreCase(SkillEntity.PublicationState.PUBLISHED, query, pageable)
            : skills.findByPublicationState(SkillEntity.PublicationState.PUBLISHED, pageable);
        List<Card> items = result.stream().filter(s -> s.getVisibility()!=SkillEntity.Visibility.HIDDEN).map(this::card).toList();
        return new SearchResponse(items, page, size, result.getTotalElements(), result.getTotalPages());
    }
    public Detail detail(String slug) {
        SkillEntity skill=skills.findBySlugAndPublicationState(slug, SkillEntity.PublicationState.PUBLISHED)
            .filter(s -> s.getVisibility()!=SkillEntity.Visibility.HIDDEN)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"SKILL_NOT_FOUND","The requested Skill is unavailable."));
        List<Version> published=versions.findBySkillIdAndPublicationStateOrderByPublishedAtDesc(skill.getId(),SkillEntity.PublicationState.PUBLISHED)
            .stream().map(v -> new Version(v.getId(),v.getSemanticVersion(),v.getGitTag(),v.getChangelog(),v.getCertificationState().name())).toList();
        return new Detail(card(skill), skill.getCuratedReport(), skill.getRepositoryRef(), published);
    }
    private Card card(SkillEntity s){
        var latest=versions.findBySkillIdAndPublicationStateOrderByPublishedAtDesc(s.getId(),SkillEntity.PublicationState.PUBLISHED).stream().findFirst();
        return new Card(s.getId(),s.getSlug(),s.getDisplayName(),s.getDescription(),s.getOwnerDisplayName(),
            new Category(s.getCategoryCode(),"IBM iSeries"),List.of("RPGLE"),latest.map(SkillVersionEntity::getSemanticVersion).orElse(null),
            latest.map(v->v.getCertificationState().name()).orElse("UNCERTIFIED"),new Rating(s.getRatingValue()==null?"UNRATED":"RATED",s.getRatingValue(),s.getRatingCount()),"VISIBLE",s.getCuratedReport());
    }
    static String normalize(String value){return value==null?"":value.trim().replaceAll("\\s+"," ");}
    static String canonicalCategory(String value){
        String n=normalize(value).toLowerCase(Locale.ROOT).replace(" ","");
        return switch(n){case "as400","ibmi","iseries","ibm_iseries" -> "IBM_ISERIES";default -> value==null?"":value.trim().toUpperCase(Locale.ROOT);};
    }
    public record SearchResponse(List<Card> items,int page,int size,long totalItems,int totalPages){}
    public record Card(UUID id,String slug,String displayName,String description,String ownerDisplayName,Category category,List<String> tags,String latestPublishedVersion,String certificationState,Rating rating,String access,String curatedPreview){}
    public record Category(String code,String label){} public record Rating(String state,java.math.BigDecimal value,int count){}
    public record Detail(Card skill,String curatedReport,String repositoryRef,List<Version> versions){}
    public record Version(UUID id,String semanticVersion,String gitTag,String changelog,String certificationState){}
}
