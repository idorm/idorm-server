package idorm.idormServer.community.domain;

import idorm.idormServer.common.domain.BaseTimeEntity;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.report.domain.Report;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    private static final int BLOCKED_CONDITION = 5;
    private static final String BLIND_COMMENT_MESSAGE = "블라인드 처리된 댓글입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    private String nickname;

    @Embedded
    private CommentContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean isDeleted;

    @OneToMany(mappedBy = "comment")
    private List<Report> reports = new ArrayList<>();

    @Builder
    private Comment(String nickname, CommentContent content, Comment parent, Post post, Member member) {
        this.nickname = nickname;
        this.content = content;
        this.parent = parent;
        this.post = post;
        this.member = member;
        this.isDeleted = false;

        post.addComment(this);
    }

    public static Comment parent(String nickname, CommentContent content, Post post, Member member) {
        return new Comment(nickname, content, null, post, member);
    }

    public static Comment child(String nickname, CommentContent content, Comment parent, Post post, Member member) {
        Comment child = new Comment(nickname, content, parent, post, member);
        parent.getChildren().add(child);
        return child;
    }

    public void validateOwner(Member member) {
        if (!member.equals(this.member)) {
            throw new CustomException(null, ExceptionCode.ACCESS_DENIED_COMMENT);
        }
    }

    public boolean isPostWriter() {
        return post.getMember().equals(member);
    }

    public void addReport(Report commentReport) {
        reports.add(commentReport);
    }

    public void deleteChild(Comment child) {
        children.remove(child);
    }

    public boolean isParent() {
        return Objects.isNull(parent);
    }

    public String getContent() {
        if (isBlocked()) {
            return BLIND_COMMENT_MESSAGE;
        }
        return content.getValue();
    }

    private boolean isBlocked() {
        return reports.size() >= BLOCKED_CONDITION;
    }

    public void delete() {
        this.isDeleted = true;
        post.deleteComment(this);
    }
}