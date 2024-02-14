package idorm.idormServer.email.application.port.in.dto;

import javax.validation.constraints.NotBlank;

public record EmailSendRequest(
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email
) {
}
