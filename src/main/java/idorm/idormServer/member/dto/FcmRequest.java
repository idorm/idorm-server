package idorm.idormServer.member.dto;

import javax.validation.constraints.NotBlank;

public record FcmRequest(
        @NotBlank(message = "FCM 토큰을 입력해주세요.")
        String fcmToken
) {
}
