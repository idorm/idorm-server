package idorm.idormServer.email.dto;

import javax.validation.constraints.NotBlank;

public record EmailSendRequest(
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email
) {
}
