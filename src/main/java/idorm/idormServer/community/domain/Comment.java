package idorm.idormServer.community.domain;//package idorm.idormServer.domain.Community;
//
//import idorm.idormServer.domain.Member;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Comment {
//    @Id
//    @GeneratedValue
//    @Column(name="comment_id")
//    private Long id;
//
//    private Timestamp createdDate; // 생성일
//    private String content; // 내용
//    private boolean visible; // 글을 보여주는지 여부
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post; // 게시글
//
//    @OneToOne
//    @JoinColumn(name = "member_id")
//    private Member commentCreator; // 댓글 작성자
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subComment_id")
//    private List<SubComment> subComments = new ArrayList<>();
//
//}
