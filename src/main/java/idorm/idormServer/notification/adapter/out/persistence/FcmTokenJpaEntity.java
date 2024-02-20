package idorm.idormServer.notification.adapter.out.persistence;

import static idorm.idormServer.notification.domain.FcmToken.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import idorm.idormServer.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenJpaEntity extends BaseTimeEntity {

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
}