package idorm.idormServer.email.domain;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.exception.ExceptionCode;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {

    public static final int CODE_LENGTH = 6;
    private static final Pattern CODE_REGEX = Pattern.compile("^\\d{3}-\\d{3}$");

    @Column(name = "code", nullable = false, length = CODE_LENGTH)
    private String value;

    @Builder
    public VerificationCode(String value) {
        this.value = value;
    }

    void verify(final String value) {
        validate(value);

        if (!this.value.equals(value.replaceFirst("-", ""))) {
            throw new CustomException(null, ExceptionCode.INVALID_CODE);
        }
    }

    public String getCode() {
        return value;
    }

    private void validate(final String value) {
        if (!CODE_REGEX.matcher(value).matches()) {
            throw new CustomException(null, ExceptionCode.INVALID_CODE);
        }
    }
}