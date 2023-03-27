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
    private int reportedCount;
    private int postLikedCnt;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<PostPhoto> postPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
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
        this.reportedCount = 0;
        this.postLikedCnt = 0;
        this.setIsDeleted(false);
    }

    public void updatePost(String title, String content, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public void addPostLikedMember(PostLikedMember postLikedMember) {
        this.postLikedMembers.add(postLikedMember);
    }

    public void removePostLikedMember(PostLikedMember postLikedMember) {
        this.postLikedMembers.remove(postLikedMember);
    }

    public List<PostLikedMember> getPostLikedMembers() {
        List<PostLikedMember> postLikedMembers = new ArrayList<>();
        for (PostLikedMember postLikedMember : this.postLikedMembers) {
            if (!postLikedMember.getIsDeleted())
                postLikedMembers.add(postLikedMember);
        }
        return postLikedMembers;
    }

    public List<PostPhoto> getPostPhotos() {
        List<PostPhoto> postPhotos = new ArrayList<>();
        for (PostPhoto postPhoto : this.postPhotos) {
            if (!postPhoto.getIsDeleted())
                postPhotos.add(postPhoto);
        }
        return postPhotos;
    }

    public List<Comment> getComments() {
        List<Comment> comments = new ArrayList<>();
        for (Comment comment : this.comments) {
            if (!comment.getIsDeleted())
                comments.add(comment);
        }
        return comments;
    }

    public void incrementPostLikedCnt() {
        this.postLikedCnt++;
    }

    public void decrementPostLikedCnt() {
        this.postLikedCnt--;
    }

    public void delete() {
        this.setIsDeleted(true);
    }

    public void removeMember() {
        this.member = null;
    }

    public void incrementReportedCount() {
        this.reportedCount += 1;
    }
}
