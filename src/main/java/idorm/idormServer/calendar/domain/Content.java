package idorm.idormServer.calendar.domain;


import idorm.idormServer.common.util.Validator;
import lombok.Getter;

@Getter
public class Content {

    private static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 100;

    private String value;

    public Content(final String value) {
        validate(value);
        this.value = value;
    }

    public static Content forMapper(final String content) {
        return new Content(content);
    }

    public void update(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        Validator.validateNotBlank(value);
        Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH, CONTENT_LENGTH_INVALID);
    }
}