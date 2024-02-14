package idorm.idormServer.member.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPhotoEmbeddedEntity {

    @Column(name = "photo_url", nullable = false)
    private String value;

    MemberPhotoEmbeddedEntity(final String photoUrl) {
        this.value = photoUrl;
    }
}