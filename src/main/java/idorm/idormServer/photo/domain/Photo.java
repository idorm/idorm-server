package idorm.idormServer.photo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity {

    @Id
    @Column(name="photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String folderName;
    private String fileName;
    private String photoUrl;
    private Boolean isDeleted; // 게시글 사진용

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member; // 프로필 사진

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // 커뮤니티 게시글


    @Builder(builderClassName = "ProfilePhotoBuilder", builderMethodName = "ProfilePhotoBuilder")
    public Photo(String folderName, String fileName, String photoUrl, Member member) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.photoUrl = photoUrl;
        this.member = member;
        this.isDeleted = false;
    }

    @Builder(builderClassName = "PostPhotoBuilder", builderMethodName = "PostPhotoBuilder")
    public Photo(String folderName, String fileName, String photoUrl, Post post) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.photoUrl = photoUrl;
        this.post = post;
        this.isDeleted = false;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
