package idorm.idormServer.member.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.adapter.out.MemberResponseCode;
import lombok.Getter;

@Getter
public class Password {

	private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$";

	private String value;

	private Password(final String value) {
		validateConstructor(value);
		this.value = value;
	}

	public static Password from(final String password) {
		return new Password(password);
	}

	public static Password forMapper(final String password) {
		return new Password(password);
	}

	private static void validateConstructor(final String value) {
		Validator.validateNotBlank(value);
		Validator.validateFormat(value, PASSWORD_REGEX, MemberResponseCode.INVALID_PASSWORD_CHARACTER);
	}
}