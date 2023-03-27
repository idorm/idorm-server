package idorm.idormServer.photo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String photoUrl;

    @Builder
    public PostPhoto(Post post, String photoUrl) {
        this.post = post;
        this.photoUrl = photoUrl;
        this.setIsDeleted(false);

        post.getPostPhotos().add(this);
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
