package idorm.idormServer.community.domain;

import static idorm.idormServer.common.exception.ExceptionCode.*;

import idorm.idormServer.common.util.Validator;
import lombok.Getter;

@Getter
public class Content {

	private static final int MIN_LENGTH = 1;
	private static final int MAX_LENGTH = 300;

	private String value;

	public Content(final String value) {
		validateConstructor(value);
		this.value = value;
	}

	public static Content forMapper(final String value) {
		return new Content(value);
	}

	private void validateConstructor(String value) {
		Validator.validateNotBlank(value);
		Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH, CONTENT_LENGTH_INVALID);
	}
}