package idorm.idormServer.auth.application.port.in.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
	@Schema(example = "knh709@inu.ac.kr")
	@NotBlank(message = "이메일을 입력해주세요.")
	String email,
	@Schema(example = "idorm2023!")
	@NotBlank(message = " 비밀번호를 입력해주세요.")
	String password,
	@Schema(example = "jfaofjaoijwjeifajiof")
	String fcmToken
) {
}
