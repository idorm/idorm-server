package idorm.idormServer.community.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private Boolean isVisible; // 댓글 삭제 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @OneToMany(mappedBy = "comment")
    private List<SubComment> subComments = new ArrayList<>();

    @Builder
    public Comment(String content, Boolean isAnonymous, Post post, Member member) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isVisible = true;
        this.member = member;
        this.post = post;
    }

    public void deleteComment() {
        this.isVisible = false;
    }
}
