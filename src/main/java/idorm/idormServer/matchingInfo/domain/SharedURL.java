package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.OPENKAKAOLINK_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SharedURL {

    public static final int MAX_URL_SIZE = 100;

    private String value;

    public SharedURL(final String value) {
        validate(value);
        this.value = value;
    }

    public static SharedURL forMapper(final String value) {
        return new SharedURL(value);
    }

    private void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateMaxLength(value, MAX_URL_SIZE, OPENKAKAOLINK_LENGTH_INVALID);
    }
}