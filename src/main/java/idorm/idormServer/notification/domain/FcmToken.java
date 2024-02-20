package idorm.idormServer.notification.domain;

import static idorm.idormServer.notification.adapter.out.FcmResponseCode.*;

import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FcmToken {

	public static final int MAX_FCM_TOKEN_LENGTH = 255;

	private Long id;
	private Long memberId;
	private String value;

	public FcmToken(final Long memberId, final String fcmToken) {
		validateConstructor(memberId, fcmToken);
		this.memberId = memberId;
		this.value = fcmToken;
	}

	public static FcmToken forMapper(final Long id,
		final Long memberId,
		final String value) {
		return new FcmToken(id, memberId, value);
	}

	public void updateToken(final String token) {
		this.value = token;
	}

	private void validateConstructor(final Long memberId, final String fcmToken) {
		Validator.validateNotNull(memberId);
		Validator.validateNotBlank(fcmToken);
		Validator.validateMaxLength(fcmToken, MAX_FCM_TOKEN_LENGTH, ILLEGAL_ARGUMENT_FCM_TOKEN);
	}
}