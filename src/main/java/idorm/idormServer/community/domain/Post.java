package idorm.idormServer.community.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    private String dormNum; // 기숙사 분류 (커뮤니티 카테고리)

    private String title; // 제목

    private String content; // 내용

    private Boolean isAnonymous; // 익명 여부

    private Boolean isDeleted; // 게시글 공개 여부

    private int likesCount; // 게사글 좋아요 수
    private int commentsCount;  // 댓글 수
    private int imagesCount; // 첨부한 사진 수
    private int reportedCount; // 신고 건수

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Photo> photos = new ArrayList<>(); // 업로드 사진들

    @OneToMany(mappedBy = "post")
    private List<PostLikedMember> postLikedMembers = new ArrayList<>(); // 게시글에 공감한 멤버들

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Member member, String dormNum, String title, String content, Boolean isAnonymous) {
        this.member = member;
        this.dormNum = dormNum;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDeleted = false;
        this.likesCount = 0;
        this.commentsCount = 0;
        this.imagesCount = 0;
        this.reportedCount = 0;
    }

    public void updatePost(String title, String content, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public void updateImagesCount(int cnt) {
        this.imagesCount = cnt;
    }

    public void addPhotos(List<Photo> photos) {
        for (Photo photo : photos) {
            this.photos.add(photo);
        }
    }

    public void deletePost() {
        this.isDeleted = true;
    }

    public void addLikesCount() {
        this.likesCount += 1;
    }

    public void deleteLikesCount() {
        this.likesCount = 0;
    }

    public void subtractLikesCount() {
        this.likesCount -= 1;
    }

    public void updateMember() {
        this.member = null;
    }
}
