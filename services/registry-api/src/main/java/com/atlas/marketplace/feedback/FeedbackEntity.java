package com.atlas.marketplace.feedback;

import jakarta.persistence.*;import java.time.Instant;import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;import org.hibernate.type.SqlTypes;
@Entity @Table(name="feedback")
public class FeedbackEntity {
 @Id private UUID id; @Column(name="actor_ref",nullable=false,length=200) private String actorRef; @Column(name="skill_id",nullable=false) private UUID skillId;
 @Column(name="skill_version_id",nullable=false) private UUID skillVersionId; @Column(name="installation_id") private UUID installationId;
 @Column(name="feedback_type",nullable=false,length=40) private String feedbackType; @Column(name="comment_text",nullable=false,length=4000) private String commentText;
 @Column(name="program_ref",length=300) private String programRef; @Column(nullable=false,length=20) private String state;
 @Column(name="independent_flag",nullable=false) private int independentFlag; @Column(name="created_at",nullable=false) private Instant createdAt;
 @Version @JdbcTypeCode(SqlTypes.NUMERIC) @Column(name="row_version",nullable=false) private long rowVersion;
 protected FeedbackEntity(){} FeedbackEntity(String actor,UUID skill,UUID version,UUID installation,String type,String comment,String program){id=UUID.randomUUID();actorRef=actor;skillId=skill;skillVersionId=version;installationId=installation;feedbackType=type;commentText=comment;programRef=program;state="OPEN";independentFlag=1;createdAt=Instant.now();}
 public UUID getId(){return id;} public UUID getSkillVersionId(){return skillVersionId;} public String getFeedbackType(){return feedbackType;} public String getState(){return state;} public String getCommentText(){return commentText;} public Instant getCreatedAt(){return createdAt;}
}
