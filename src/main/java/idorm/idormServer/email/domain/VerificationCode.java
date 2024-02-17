package idorm.idormServer.email.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;
import idorm.idormServer.email.adapter.out.api.exception.InvalidVerificationCode;
import lombok.Getter;

@Getter
public class VerificationCode {

	public static final int CODE_LENGTH = 7;
	private static final String CODE_REGEX = "^\\d{3}-\\d{3}$";

	private String value;

	public VerificationCode(final String value) {
		validate(value);
		this.value = value;
	}

	private static void validate(final String value) {
		Validator.validateNotBlank(value);
		Validator.validateFormat(value, CODE_REGEX, EmailResponseCode.INVALID_VERIFICATION_CODE);
	}

	public static VerificationCode forMapper(final String code) {
		return new VerificationCode(code);
	}

	void match(String value) {
		validate(value);

		if (!this.value.equals(value)) {
			throw new InvalidVerificationCode();
		}
	}
}