package idorm.idormServer.member.domain;

import idorm.idormServer.common.util.Validator;
import lombok.Getter;

@Getter
public class MemberPhoto {

    private String value;

    public MemberPhoto(final String photoUrl) {
        validate(photoUrl);
        this.value = photoUrl;
    }

    public static MemberPhoto forMapper(final String photoUrl) {
        return new MemberPhoto(photoUrl);
    }

    private void validate(String photoUrl) {
        Validator.validateNotBlank(photoUrl);
    }
}