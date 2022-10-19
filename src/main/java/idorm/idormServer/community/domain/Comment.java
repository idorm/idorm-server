//package idorm.idormServer.community.domain;
//
//import idorm.idormServer.common.BaseEntity;
//import idorm.idormServer.member.domain.Member;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Comment extends BaseEntity {
//    @Id
//    @GeneratedValue
//    @Column(name="comment_id")
//    private Long id;
//
//    private String content; // 내용
//    private Boolean isAnonymous; // 익명 여부, default는 true(익명)
//    private Boolean isVisible; // 댓글 공개 여부
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post; // 게시글
//
//    @OneToOne
//    @JoinColumn(name = "member_id")
//    private Member member; // 댓글 작성자
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sub_comment_id")
//    private List<SubComment> subComments = new ArrayList<>();
//
//    @Builder
//    public Comment(String content, Post post, Member member) {
//        this.content = content;
//        this.isVisible = true;
//        this.member = member;
//        this.post = post;
//    }
//
//    public void deleteComment() {
//        this.isVisible = false;
//    }
//}
