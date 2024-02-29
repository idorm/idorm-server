package idorm.idormServer.auth.application.port.in.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
//	@NotBlank(message = "이메일을 입력해주세요.")
	String email,
//	@NotBlank(message = " 비밀번호를 입력해주세요.")
	String password,
	String fcmToken
) {
}
