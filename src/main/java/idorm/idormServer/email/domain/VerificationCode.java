package idorm.idormServer.email.domain;

import static idorm.idormServer.common.exception.ExceptionCode.INVALID_CODE;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {

    public static final int CODE_LENGTH = 7;
    private static final String CODE_REGEX = "^\\d{3}-\\d{3}$";

    @Column(name = "code", nullable = false, length = CODE_LENGTH)
    private String value;

    @Builder
    public VerificationCode(final String value) {
        validate(value);
        this.value = value;
    }

    void verify(final String value) {
        validate(value);

        if (!this.value.equals(value)) {
            throw new CustomException(null, INVALID_CODE);
        }
    }

    public String getCode() {
        return value;
    }

    private void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateFormat(value, CODE_REGEX, INVALID_CODE);
    }
}