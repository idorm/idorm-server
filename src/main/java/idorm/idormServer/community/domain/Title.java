package idorm.idormServer.community.domain;

import static idorm.idormServer.common.exception.ExceptionCode.TITLE_LENGTH_INVALID;

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
public class Title {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;

    @NotBlank
    @Column(name = "title", nullable = false, length = MAX_LENGTH)
    private String value;

    public Title(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        Validator.validateNotBlank(value);
        Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH, TITLE_LENGTH_INVALID);
    }
}