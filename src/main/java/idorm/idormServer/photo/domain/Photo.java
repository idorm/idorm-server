package idorm.idormServer.photo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Photo extends BaseEntity {

    @Id
    @Column(name="photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(length = 1000)
    private String url;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member; // 프로필 사진

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // 커뮤니티 게시글

    public Photo(String fileName, String url, Member member) {
        this.fileName = fileName;
        this.url = url;
        this.member = member;
    }

    public Photo(String fileName, String url, Member member, Post post) {
        this.fileName = fileName;
        this.url = url;
        this.member = member;
        this.post = post;
    }

    public void updateFileName(String fileName) {
        this.fileName = fileName;
    }

    public void updateUrl(String url) {
        this.url = url;
    }
}
