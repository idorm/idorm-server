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
public class MemberPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String photoUrl;

    @Builder
    public MemberPhoto(Member member, String photoUrl) {
        this.member = member;
        this.photoUrl = photoUrl;
        this.setIsDeleted(false);

        if (!member.getMemberPhotos().contains(this))
            member.getMemberPhotos().add(this);
    }

    public void removePhotoUrl() {
        this.photoUrl = null;
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
