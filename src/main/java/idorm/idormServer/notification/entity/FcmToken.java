package idorm.idormServer.notification.entity;

import static idorm.idormServer.notification.adapter.out.FcmResponseCode.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import idorm.idormServer.common.entity.BaseTimeEntity;
import idorm.idormServer.common.util.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken extends BaseTimeEntity {

	public static final int MAX_FCM_TOKEN_LENGTH = 255;

	@Id
	@Column(name = "fcm_token_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "token", nullable = false)
	@Size(max = MAX_FCM_TOKEN_LENGTH)
	private String value;

	public FcmToken(final Long memberId, final String fcmToken) {
		validateConstructor(memberId, fcmToken);
		this.memberId = memberId;
		this.value = fcmToken;
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