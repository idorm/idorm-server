//package idorm.idormServer.domain.Community;
//
//import idorm.idormServer.domain.Dormitory;
//import idorm.idormServer.domain.Member;
//import idorm.idormServer.domain.Photo;
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
//public class Post {
//
//    @Id
//    @GeneratedValue
//    @Column(name="post_id")
//    private Long id;
//
//    private Timestamp createdDate; // 생성일
//    private Timestamp lastModifiedDate; // 수정일
//    private String title; // 제목
//    private String content; // 내용
//    private Integer likes; // 좋아요 수
//    private boolean visible; // 글을 보여주는지 여부
//
//    @OneToOne
//    @JoinColumn(name = "member_id")
//    private Member postCreator; // 작성자
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dormCategory_id")
//    private DormCategory dormCategory; // 커뮤니티 카테고리
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "photo_id")
//    private List<Photo> photos = new ArrayList<>(); // 업로드 사진들
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id")
//    private List<Comment> comments = new ArrayList<>();
//
//
//
//
//}
