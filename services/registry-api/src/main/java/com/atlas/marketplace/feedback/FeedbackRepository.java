package com.atlas.marketplace.feedback;
import java.util.List;import java.util.UUID;import org.springframework.data.jpa.repository.JpaRepository;
interface FeedbackRepository extends JpaRepository<FeedbackEntity,UUID>{long countBySkillVersionIdAndFeedbackTypeAndIndependentFlag(UUID version,String type,int independent);List<FeedbackEntity> findByActorRefOrderByCreatedAtDesc(String actor);}
