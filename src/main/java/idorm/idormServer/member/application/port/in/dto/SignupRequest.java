package idorm.idormServer.member.application.port.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record SignupRequest(
	@Schema(example = "knh709@inu.ac.kr")
	@Email(message = "email , 유효한 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 공백일 수 없습니다.")
	String email,

	@Schema(example = "idorm2023!")
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	String password,

	@Schema(example = "나도미")
	@NotBlank(message = "닉네임은 공백일 수 없습니다.")
	String nickname
) {
}