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
    @GeneratedValue
    @Column(name = "sub_comment_id")
    private Long id;

    private String content;
    private Boolean isVisible;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member; // 대댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public SubComment(String content, Member member, Comment comment) {
        this.content = content;
        this.isVisible = true;
        this.member = member;
        this.comment = comment;
    }
}
