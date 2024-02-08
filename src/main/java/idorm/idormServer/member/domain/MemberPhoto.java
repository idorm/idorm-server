package idorm.idormServer.member.domain;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPhoto {

    @Column(name = "photo_url", nullable = false)
    private String value;

    public MemberPhoto(Member member, String photoUrl) {
        validate(photoUrl);
        this.value = photoUrl;

        member.updateMemberPhoto(this);
    }

    private void validate(String photoUrl) {
        Validator.validateNotBlank(photoUrl);
    }
}