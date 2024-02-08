package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.OPENKAKAOLINK_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SharedURL {

    private static final int MAX_URL_SIZE = 100;

    @Column(nullable = false, name = "shared_url", length = MAX_URL_SIZE)
    private String value;

    public SharedURL(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateMaxLength(value, MAX_URL_SIZE, OPENKAKAOLINK_LENGTH_INVALID);
    }
}
