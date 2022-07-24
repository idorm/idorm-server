package idorm.idormServer.domain.Community;

import idorm.idormServer.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubComment {

    @Id
    @GeneratedValue
    @Column(name = "subComment_id")
    private Long id;

    private Timestamp createdAt; // 생성일
    private String content; // 대댓글 내용
    private boolean visible; // 글을 보여주는지 여부

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member subCommentCreator; // 댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
