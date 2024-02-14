package idorm.idormServer.email.adapter.in.web;

import idorm.idormServer.common.dto.DefaultResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "9. Email", description = "이메일 인증 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailUseCase emailUseCase;

    @Operation(summary = "[회원가입용] 이메일 인증코드 발송")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "SEND_EMAIL",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "EMAIL_CHARACTER_INVALID / FIELD_REQUIRED"),
            @ApiResponse(responseCode = "409", description = "DUPLICATE_EMAIL"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR / EMAIL_SERVER_ERROR"),
    }
    )
    @PostMapping("/signup/email/send")
    public ResponseEntity<DefaultResponseDto<Object>> sendAuthenticationEmail(
            @RequestBody @Valid EmailSendRequest request) {

        emailUseCase.sendVerificationEmail(request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("SEND_EMAIL")
                        .responseMessage("이메일 인증코드 전송 완료")
                        .build());

    }


    @Operation(summary = "[회원가입용] 이메일 인증코드 검증", description = "/email 인증코드 확인 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "EMAIL_VERIFIED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "INVALID_CODE / EXPIRED_CODE"),
            @ApiResponse(responseCode = "404", description = "EMAIL_NOT_FOUND"),
            @ApiResponse(responseCode = "409", description = "DUPLICATE_MEMBER"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PostMapping("/signup/email/verification")
    public ResponseEntity<DefaultResponseDto<Object>> verifyAuthenticationCode(
            @RequestBody @Valid EmailVerifyRequest request) {

        emailUseCase.verifyCode(request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("EMAIL_VERIFIED")
                        .responseMessage("이메일 인증코드 검증 완료")
                        .build());
    }

    @Operation(summary = "[비밀번호 수정용] 이메일 인증코드 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SEND_REGISTERED_EMAIL",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PostMapping("/members/me/password/email/send")
    public ResponseEntity<DefaultResponseDto<Object>> sendReAuthenticationEmail(
            @RequestBody @Valid EmailSendRequest request) {

        emailUseCase.sendReverificationEmail(request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("SEND_REGISTERED_EMAIL")
                        .responseMessage("등록된 이메일 인증코드 전송 완료")
                        .build());

    }

    @Operation(summary = "[비밀번호 수정용] 이메일 인증코드 검증", description = "/email/password 인증코드 확인 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "REGISTERED_EMAIL_VERIFIED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "INVALID_CODE / EXPIRED_CODE"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    }
    )
    @PostMapping("/members/me/password/email/verification")
    public ResponseEntity<DefaultResponseDto<Object>> verifyReAuthenticationCode(
            @RequestBody EmailVerifyRequest request) {

        emailUseCase.reVerifyCode(request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("REGISTERED_EMAIL_VERIFIED")
                        .responseMessage("등록된 이메일 인증코드 검증 완료")
                        .build());
    }
}