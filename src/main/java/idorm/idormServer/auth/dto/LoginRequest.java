package idorm.idormServer.auth.dto;

import javax.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,
        @NotBlank(message = " 비밀번호를 입력해주세요.")
        String password
) {
}
