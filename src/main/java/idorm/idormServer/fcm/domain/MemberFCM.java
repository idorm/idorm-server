package idorm.idormServer.fcm.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.fcm.adapter.out.FcmResponseCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberFCM {

  public static final int MAX_FCM_TOKEN_LENGTH = 255;

  @Id
  @Column(name = "member_fcm_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "fcm_token", nullable = false)
  @Size(max = MAX_FCM_TOKEN_LENGTH)
  private String value;

  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  public MemberFCM(final Long memberId, final String fcmToken) {
    validate(memberId, fcmToken);
    this.memberId = memberId;
    this.value = fcmToken;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public static MemberFCM forMapper(final Long id,
      final Long memberId,
      final String value,
      final LocalDateTime createdAt,
      final LocalDateTime updatedAt) {
    return new MemberFCM(id, memberId, value, createdAt, updatedAt);
  }

  private void validate(final Long memberId, final String fcmToken) {
    Validator.validateNotNull(memberId);
    Validator.validateNotBlank(fcmToken);
    Validator.validateMaxLength(fcmToken, MAX_FCM_TOKEN_LENGTH,
        FcmResponseCode.ILLEGAL_ARGUMENT_FCM_TOKEN);
  }
}