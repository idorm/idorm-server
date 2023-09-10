package idorm.idormServer.member.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Email;
import idorm.idormServer.member.dto.EmailAuthRequest;
import idorm.idormServer.member.dto.EmailVerifyRequest;
import idorm.idormServer.member.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;
import static idorm.idormServer.config.SecurityConfiguration.AUTHENTICATION_URL;
import static idorm.idormServer.exception.ExceptionCode.*;

@Tag(name = "Email", description = "이메일 인증 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1 + AUTHENTICATION_URL)
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

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
    @PostMapping("/email")
    public ResponseEntity<DefaultResponseDto<Object>> sendAuthEmail(
            @RequestBody @Valid EmailAuthRequest request) {


        emailService.validateEmailDomain(request.getEmail());

        Optional<Email> existingEmail = emailService.isExistingEmail(request.getEmail());

        boolean emailExistence = emailService.validateEmailExistence(existingEmail);
        if (emailExistence)
            emailService.delete(existingEmail.get());

        String verificationCode = emailService.createVerificationCode();
        Email email = emailService.save(request.toEntity(verificationCode));
        emailService.sendVerificationEmail(email);

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
    @PostMapping("/verifyCode/{email}")
    public ResponseEntity<DefaultResponseDto<Object>> verifyCode(
            @PathVariable("email") String requestEmail,
            @RequestBody @Valid EmailVerifyRequest request) {

        Optional<Email> existingEmail = emailService.isExistingEmail(requestEmail);

        if (existingEmail.isEmpty())
            throw new CustomException(null, EMAIL_NOT_FOUND);
        else {

            if (existingEmail.get().getIsCheck()) {
                if (existingEmail.get().getMember() != null) {
                    if (!existingEmail.get().getMember().getIsDeleted()) {
                        throw new CustomException(null, DUPLICATE_MEMBER);
                    } else {
                        emailService.delete(existingEmail.get());
                    }
                } else {
                    emailService.delete(existingEmail.get());
                }
            }
        }

        emailService.validateEmailIsExpired(existingEmail.get().getCreatedAt());
        emailService.validateEmailCodeIsValid(existingEmail.get(), request.getCode());

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
    @PostMapping("/email/password")
    public ResponseEntity<DefaultResponseDto<Object>> findPassword(
            @RequestBody @Valid EmailAuthRequest request) {

        emailService.validateEmailDomain(request.getEmail());

        Email foundEmail = emailService.findMemberByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));

        String verificationCode = emailService.createVerificationCode();
        emailService.updateVerificationCode(foundEmail, verificationCode);

        emailService.sendVerificationEmail(foundEmail);
        emailService.updateIsPossibleUpdatePassword(foundEmail, false);

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
    @PostMapping("/verifyCode/password/{email}")
    public ResponseEntity<DefaultResponseDto<Object>> verifyCodePassword(
            @PathVariable("email") String requestEmail,
            @RequestBody EmailVerifyRequest request) {

        emailService.validateEmailDomain(requestEmail);

        Email foundEmail = emailService.findMemberByEmail(requestEmail)
                .orElseThrow(() -> new CustomException(null, MEMBER_NOT_FOUND));

        emailService.validateEmailIsExpired(foundEmail.getUpdatedAt());
        emailService.validateEmailCodeIsValid(foundEmail, request.getCode());
        emailService.updateIsPossibleUpdatePassword(foundEmail, true);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("REGISTERED_EMAIL_VERIFIED")
                        .responseMessage("등록된 이메일 인증코드 검증 완료")
                        .build());
    }
}