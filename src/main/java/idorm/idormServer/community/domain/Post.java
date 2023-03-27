package idorm.idormServer.community.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.PostPhoto;
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

    private Character dormCategory;
    private String title;
    private String content;
    private Boolean isAnonymous;
    private int postLikedCnt; // Repository 정렬 조건용, 삭제 금지
    private int reportedCount;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<PostPhoto> postPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLikedMember> postLikedMembers = new ArrayList<>(); // 게시글에 공감한 멤버들

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Member member, DormCategory dormCategory, String title, String content, Boolean isAnonymous) {
        this.member = member;
        this.dormCategory = dormCategory.getType();
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.postLikedCnt = 0;
        this.reportedCount = 0;
        this.setIsDeleted(false);

        member.getPosts().add(this);
    }

    public void updatePost(String title, String content, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public List<PostLikedMember> getPostLikedMembersIsDeletedIsFalse() {
        List<PostLikedMember> postLikedMembers = new ArrayList<>();
        for (PostLikedMember postLikedMember : this.postLikedMembers) {
            if (!postLikedMember.getIsDeleted())
                postLikedMembers.add(postLikedMember);
        }
        return postLikedMembers;
    }

    public List<PostPhoto> getPostPhotosIsDeletedIsFalse() {
        List<PostPhoto> postPhotos = new ArrayList<>();
        for (PostPhoto postPhoto : this.postPhotos) {
            if (!postPhoto.getIsDeleted())
                postPhotos.add(postPhoto);
        }
        return postPhotos;
    }

    public List<Comment> getCommentsIsDeletedIsFalse() {
        List<Comment> comments = new ArrayList<>();
        for (Comment comment : this.comments) {
            if (!comment.getIsDeleted())
                comments.add(comment);
        }
        return comments;
    }

    public void delete() {
        this.setIsDeleted(true);
    }

    public void incrementPostLikedCnt() {
        this.postLikedCnt += 1;
    }

    public void decrementPostLikedCnt() {
        this.postLikedCnt -= 1;
    }

    public void incrementReportedCount() {
        this.reportedCount += 1;
    }
}
