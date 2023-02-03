package idorm.idormServer.email.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.dto.EmailAuthRequestDto;
import idorm.idormServer.email.dto.EmailDefaultResponseDto;
import idorm.idormServer.email.dto.EmailVerifyRequestDto;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.exception.DefaultExceptionResponseDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

import static idorm.idormServer.exception.ExceptionCode.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "Email API")
public class EmailController {

    private final EmailService emailService;
    private final MemberService memberService;

    private final JavaMailSender emailSender;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "Email 인증")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = EmailDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "EMAIL_FORMAT_INVALID / FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_EMAIL",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    }
    )
    @PostMapping("/email")
    public ResponseEntity<DefaultResponseDto<Object>> emailAuth(
            @RequestBody @Valid EmailAuthRequestDto request) throws MessagingException, UnsupportedEncodingException {

        String requestEmail = request.getEmail();
        Optional<Email> email = emailService.findByEmailOp(request.getEmail());

        if(email.isPresent()) {
            Optional<Long> foundMemberId = memberService.findByEmailOp(requestEmail);

            if(foundMemberId.isPresent()) {
                throw new CustomException(DUPLICATE_EMAIL);
            }
        }

        String[] mailSplit = requestEmail.split("@");

        if(!(mailSplit.length == 2) || !mailSplit[1].equals("inu.ac.kr")) {
            throw new CustomException(EMAIL_FORMAT_INVALID);
        }

        sendSimpleMessage(requestEmail);

        EmailDefaultResponseDto response = null;
        if(!email.isEmpty()) {
            response = new EmailDefaultResponseDto(email.get());
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Email 인증코드 전송 완료")
                        .data(response)
                        .build());

    }


    @ApiOperation(value = "Email 인증코드 검증", notes = "/email 인증코드 확인 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = EmailDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "INVALID_CODE / EXPIRED_CODE",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    }
    )
    @PostMapping("/verifyCode/{email}")
    public ResponseEntity<DefaultResponseDto<Object>> verifyCode(
            @PathVariable("email") String requestEmail,
            @RequestBody @Valid EmailVerifyRequestDto code) {

        Email email = emailService.findByEmail(requestEmail);

        LocalDateTime updateDateTime = email.getUpdatedAt();
        LocalDateTime expiredDateTime = updateDateTime.plusMinutes(5);

        if(!(email.getCode().equals(code.getCode()))) {
            throw new CustomException(INVALID_CODE);
        }

        if(LocalDateTime.now().isAfter(expiredDateTime)) {
            throw new CustomException(EXPIRED_CODE);
        }

        emailService.isChecked(requestEmail);
        EmailDefaultResponseDto response = new EmailDefaultResponseDto(email);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Email 인증코드 검증 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "비밀번호 수정용 Email 인증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = EmailDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "EMAIL_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    }
    )
    @PostMapping("/email/password")
    public ResponseEntity<DefaultResponseDto<Object>> findPassword(
            @RequestBody @Valid EmailAuthRequestDto request) throws MessagingException, UnsupportedEncodingException {

        String requestEmail = request.getEmail();

        memberService.findByEmail(requestEmail);
        Email email = emailService.findByEmail(requestEmail);

        sendSimpleMessage(requestEmail);
        emailService.updateUpdatedAt(email);

        EmailDefaultResponseDto response = new EmailDefaultResponseDto(email);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Email 인증코드 전송 완료")
                        .data(response)
                        .build());

    }

    @ApiOperation(value = "비밀번호 수정용 Email 인증코드 검증", notes = "/email/password 인증코드 확인 용도")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = EmailDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "INVALID_CODE / EXPIRED_CODE",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = DefaultExceptionResponseDto.class))),
    }
    )
    @PostMapping("/verifyCode/password/{email}")
    public ResponseEntity<DefaultResponseDto<Object>> verifyCodePassword(
            @PathVariable("email") String requestEmail, @RequestBody EmailVerifyRequestDto code) {

        Optional<Email> email = emailService.findByEmailOp(requestEmail);
        Member member = memberService.findByEmail(requestEmail);

        LocalDateTime updateDateTime = email.get().getUpdatedAt();
        LocalDateTime expiredDateTime = updateDateTime.plusMinutes(5);

        if(!(email.get().getCode().equals(code.getCode()))) {
            throw new CustomException(INVALID_CODE);
        }

        if(LocalDateTime.now().isAfter(expiredDateTime)) {
            throw new CustomException(EXPIRED_CODE);
        }

        Iterator<String> iter = member.getRoles().iterator();
        List<String> roles = new ArrayList<>();

        while (iter.hasNext()) {
            roles.add(iter.next());
        }

        jwtTokenProvider.createToken(member.getUsername(), roles);
        EmailDefaultResponseDto response = new EmailDefaultResponseDto(email.get());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Email 인증코드 검증 완료")
                        .data(response)
                        .build());

    }

    /**
     *함수
     */
    //이메일,인증번호로그/DB 저장
    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : " + to);

        MimeMessage  message = emailSender.createMimeMessage();
        String ePw = createKey();
        log.info("인증 번호 : " + ePw);
        String code = createCode(ePw);
        message.addRecipients(MimeMessage.RecipientType.TO, to); //보내는 대상
        message.setSubject("IDORM 확인 코드: " + code,"UTF-8"); //제목

        if(emailService.findByEmailOp(to).isEmpty()) {
            emailService.save(to, code);
        }
        else{
            emailService.setCode(to,code);
        }

        String msg="";
        msg += "<img width=\"120\" height=\"36\" style=\"margin-top: 0; margin-right: 0; margin-bottom: 32px; margin-left: 0px; padding-right: 30px; padding-left: 30px;\" src=\"https://slack.com/x-a1607371436052/img/slack_logo_240.png\" alt=\"\" loading=\"lazy\">";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 Slack 가입 창이 있는 브라우저 창에 입력하세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += code;
        msg += "</td></tr></tbody></table></div>";
        msg += "<a href=\"https://slack.com\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">Slack Clone Technologies, Inc</a>";

        message.setText(msg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress(to,"slack-clone")); //보내는 사람

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));

        }
        return key.toString();
    }

    public String createCode(String ePw){
        return ePw.substring(0, 3) + "-" + ePw.substring(3, 6);
    }

    //전송
    public void sendSimpleMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = createMessage(to);

        try{
            emailSender.send(message);
        } catch(MailException e){
            log.info(e.getMessage());
            throw new CustomException(EMAIL_NOT_FOUND);
        }
    }
}