package idorm.idormServer.photo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity {

    @Id
    @Column(name="photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String folderName;

    private String url;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member; // 프로필 사진

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // 커뮤니티 게시글

    public Photo(String folderName, String fileName, String url, Member member) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.url = url;
        this.member = member;
    }

    public Photo(String folderName, String fileName, String url, Member member, Post post) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.url = url;
        this.member = member;
        this.post = post;
    }
}
