package idorm.idormServer.community.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String photoUrl;

    private PostPhoto(Post post, String photoUrl) {
        this.post = post;
        this.photoUrl = photoUrl;
    }

    public static List<PostPhoto> of(Post post, ArrayList<String> photoUrls) {
        List<PostPhoto> postPhotos = new ArrayList<>();

        photoUrls.forEach(photoUrl -> postPhotos.add(new PostPhoto(post, photoUrl)));
        return postPhotos;
    }
}