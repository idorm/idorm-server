package idorm.idormServer.community.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.matchingInfo.domain.DormCategory;
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

    private Character dormCategory;
    private String title;
    private String content;
    private Boolean isAnonymous;
    private int reportedCount;
    private Boolean isDeleted;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Photo> photos = new ArrayList<>(); // 업로드 사진들

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
        this.isDeleted = false;
    }

    public void updatePost(String title, String content, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public void addPostPhotos(List<Photo> photos) {
        for (Photo photo : photos) {
            this.photos.add(photo);
        }
    }

    public void deletePost() {
        this.isDeleted = true;
    }

    public void removeMember() {
        this.member = null;
    }
}
