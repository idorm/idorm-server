package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.AGE_LENGTH_INVALID;

import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Age {

    private static final int MIN_AGE_SIZE = 20;
    private static final int MAX_AGE_SIZE = 50;

    @Column(name = "age", nullable = false)
    private Integer value;

    public Age(final Integer value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Integer value) {
        Validator.validateNotNull(value);
        Validator.validateSize(value, MIN_AGE_SIZE, MAX_AGE_SIZE, AGE_LENGTH_INVALID);
    }
}
