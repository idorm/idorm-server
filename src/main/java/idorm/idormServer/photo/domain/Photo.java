package idorm.idormServer.photo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Photo(String fileName, String url, Member member) {
        this.fileName = fileName;
        this.url = url;
        this.member = member;
    }

    public void updateFileName(String fileName) {
        this.fileName = fileName;
    }

    public void updateUrl(String url) {
        this.url = url;
    }
}
