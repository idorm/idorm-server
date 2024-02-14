package idorm.idormServer.member.application.port.in.dto;

import javax.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email,
        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        String password,
        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        String nickname
) {
}