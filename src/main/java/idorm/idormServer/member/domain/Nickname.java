package idorm.idormServer.member.domain;

import java.time.LocalDateTime;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.adapter.out.MemberResponseCode;
import idorm.idormServer.member.adapter.out.exception.CannotUpdateNicknameException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "value")
public class Nickname {

	private static final String NICKNAME_REGEX = "^[A-Za-z0-9ㄱ-ㅎ가-힣]{2,8}+$";
	private static final int VALID_UPDATE_DAY = 30;

	private String value;
	private LocalDateTime createdAt;

	private Nickname(final String value) {
		validate(value);
		this.value = value;
		this.createdAt = LocalDateTime.now();
	}

	public static Nickname from(final String value) {
		return new Nickname(value);
	}

	private void validate(final String value) {
		Validator.validateNotBlank(value);
		Validator.validateFormat(value, NICKNAME_REGEX, MemberResponseCode.INVALID_NICKNAME_CHARACTER);

	}

	public static Nickname forMapper(final String nickname) {
		return new Nickname(nickname);
	}

	void validateValidUpdate(final LocalDateTime now) {
		if (now.isBefore(this.createdAt.plusDays(VALID_UPDATE_DAY))) {
			throw new CannotUpdateNicknameException();
		}
	}
}