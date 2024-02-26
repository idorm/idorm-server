package idorm.idormServer.member.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.adapter.out.MemberResponseCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

	private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$";

	@Column(name = "password", nullable = false)
	private String value;

	private Password(final String value) {
		this.value = value;
	}

	public static Password from(final EncryptPort encryptPort, final String password) {
		validateConstructor(password);
		return new Password(encryptPort.encrypt(password));
	}

	void update(final EncryptPort encryptPort, final String newPassword) {
		validateConstructor(newPassword);
		this.value = encryptPort.encrypt(newPassword);
	}

	private static void validateConstructor(final String value) {
		Validator.validateNotBlank(value);
		Validator.validateFormat(value, PASSWORD_REGEX, MemberResponseCode.INVALID_PASSWORD_CHARACTER);
	}
}