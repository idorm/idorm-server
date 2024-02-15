package idorm.idormServer.community.domain;

import static idorm.idormServer.common.exception.ExceptionCode.*;

import idorm.idormServer.common.util.Validator;
import lombok.Getter;

@Getter
public class Title {

	private static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 50;

	private String value;

	public Title(final String value) {
		validateConstructor(value);
		this.value = value;
	}

	public static Title forMapper(final String title) {
		return new Title(title);
	}

	private void validateConstructor(String value) {
		Validator.validateNotBlank(value);
		Validator.validateLength(value, MIN_LENGTH, MAX_LENGTH, TITLE_LENGTH_INVALID);
	}
}