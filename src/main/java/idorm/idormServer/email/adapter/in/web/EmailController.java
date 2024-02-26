package idorm.idormServer.email.adapter.in.web;

import static idorm.idormServer.email.adapter.out.api.EmailResponseCode.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.common.response.SuccessResponse;
import idorm.idormServer.email.application.port.in.EmailUseCase;
import idorm.idormServer.email.application.port.in.dto.EmailSendRequest;
import idorm.idormServer.email.application.port.in.dto.EmailVerifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. Email", description = "이메일 인증 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

	private final EmailUseCase emailUseCase;

	@Operation(summary = "[회원가입용] 이메일 인증코드 발송")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "SEND_EMAIL",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "EMAIL_CHARACTER_INVALID / FIELD_REQUIRED"),
		@ApiResponse(responseCode = "409", description = "DUPLICATE_EMAIL"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR / EMAIL_SERVER_ERROR"),
	})
	@PostMapping("/verify")
	public ResponseEntity<SuccessResponse<Object>> sendAuthenticationEmail(
		@RequestBody @Valid EmailSendRequest request) {

		emailUseCase.sendVerificationEmail(request);

		return ResponseEntity.ok().body(SuccessResponse.from(SEND_EMAIL));
	}

	@Operation(summary = "[회원가입용] 이메일 인증코드 검증", description = "/email 인증코드 확인 용도")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "EMAIL_VERIFIED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
		@ApiResponse(responseCode = "401", description = "INVALID_VERIFICATION_CODE / EXPIRED_EMAIL_VERIFICATION_CODE"),
		@ApiResponse(responseCode = "404", description = "EMAIL_NOT_FOUND"),
		@ApiResponse(responseCode = "409", description = "DUPLICATE_EMAIL"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PostMapping("/verification")
	public ResponseEntity<SuccessResponse<Object>> verifyAuthenticationCode(
		@RequestBody @Valid EmailVerifyRequest request) {

		emailUseCase.verifyCode(request);

		return ResponseEntity.ok().body(SuccessResponse.from(EMAIL_VERIFIED));
	}

	@Operation(summary = "[비밀번호 수정용] 이메일 인증코드 발송")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "SEND_REGISTERED_EMAIL",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_EMAIL / NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PostMapping("/reverify")
	public ResponseEntity<SuccessResponse<Object>> sendReAuthenticationEmail(
		@RequestBody @Valid EmailSendRequest request) {

		emailUseCase.sendReverificationEmail(request);

		return ResponseEntity.ok().body(SuccessResponse.from(SEND_REGISTERED_EMAIL));
	}

	@Operation(summary = "[비밀번호 수정용] 이메일 인증코드 검증", description = "/email/password 인증코드 확인 용도")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "REGISTERED_EMAIL_VERIFIED",
			content = @Content(schema = @Schema(implementation = Object.class))),
		@ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
		@ApiResponse(responseCode = "401", description = "INVALID_VERIFICATION_CODE / EXPIRED_EMAIL_VERIFICATION_CODE"),
		@ApiResponse(responseCode = "404", description = "NOT_FOUND_EMAIL / NOT_FOUND_MEMBER"),
		@ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
	}
	)
	@PostMapping("/reverification")
	public ResponseEntity<SuccessResponse<Object>> verifyReAuthenticationCode(
		@RequestBody EmailVerifyRequest request) {

		emailUseCase.reVerifyCode(request);

		return ResponseEntity.ok().body(SuccessResponse.from(REGISTERED_EMAIL_VERIFIED));
	}
}