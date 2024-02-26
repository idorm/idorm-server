package idorm.idormServer.email.application.port.in.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmailSendRequest(
	@Schema(example = "babo@inu.ac.kr")
	@NotBlank(message = "이메일은 공백일 수 없습니다.")
	String email
) {
}
