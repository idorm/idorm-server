package idorm.idormServer.community.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    private String content; // 내용
    private Boolean isAnonymous; // 익명 여부
    private Long parentCommentId; // 부모 댓글 식별자
    private Integer reportedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(String content, Boolean isAnonymous, Post post, Member member) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.member = member;
        this.post = post;
        this.reportedCount = 0;
        this.setIsDeleted(false);
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public void delete() {
        this.setIsDeleted(true);
    }

    public void updateMemberNull() {
        this.member = null;
    }

    public void incrementReportedCount() {
        this.reportedCount += 1;
    }
}
