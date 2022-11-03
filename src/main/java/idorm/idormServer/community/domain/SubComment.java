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
public class SubComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_comment_id")
    private Long id;

    private String content;
    private Boolean isAnonymous; // 익명 여부
    private Boolean isVisible; // 대댓글 삭제여부

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public SubComment(String content, Boolean isAnonymous, Member member, Comment comment) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isVisible = true;
        this.member = member;
        this.comment = comment;
    }

    public void deleteSubComment() {
        this.isVisible = false;
    }
}
