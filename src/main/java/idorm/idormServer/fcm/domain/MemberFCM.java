package idorm.idormServer.fcm.domain;

import idorm.idormServer.common.domain.BaseTimeEntity;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_fcm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFCM extends BaseTimeEntity {

    private static final int MAX_FCM_TOKEN_LENGTH = 255;

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

    public MemberFCM(final Long memberId, final String fcmToken) {
        validate(memberId, fcmToken);
        this.memberId = memberId;
        this.value = fcmToken;
    }

    private void validate(final Long memberId, final String fcmToken) {
        Validator.validateNotNull(memberId);
        Validator.validateNotBlank(fcmToken);
        Validator.validateMaxLength(fcmToken, MAX_FCM_TOKEN_LENGTH, ExceptionCode.ILLEGAL_ARGUMENT_FCM_TOKEN);
    }
}