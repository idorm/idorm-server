package idorm.idormServer.calendar.domain;

import static idorm.idormServer.common.exception.ExceptionCode.CONTENT_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    @NotBlank
    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private String value;

    public Content(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        Validator.validateNotBlank(value);
        Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH, CONTENT_LENGTH_INVALID);
    }
}