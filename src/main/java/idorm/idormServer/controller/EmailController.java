package idorm.idormServer.controller;

import idorm.idormServer.domain.Member;
import idorm.idormServer.dto.EmailRequest;
import idorm.idormServer.dto.EmailResponseDto;
import idorm.idormServer.dto.VerifyRequest;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.EmailService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;
    private final JavaMailSender emailSender;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    @ApiOperation(value = "이메일 인증", notes = "학교 이메일 형식(@inu.ac.kr)으로 이메일을 받아서 해당 이메일로 인증코드를 발송합니다.")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "이메일이 전송되었습니다."),
//            @ApiResponse(code = 400, message = "형식에 맞지 않는 이메일입니다.")
//    })
    @PostMapping("/email") // 이메일 인증 코드 보내기
    public EmailResponseDto emailAuth(@RequestBody @Valid EmailRequest email) throws Exception {

        String[] mailSplit=email.getEmail().split("@");

        if(!(mailSplit.length==2)) {
            throw new IllegalArgumentException("형식에 맞지않는 이메일입니다.");
        }

        if(mailSplit[1].equals("inu.ac.kr")) {
            if (emailService.findByEmailOp(email.getEmail()).isEmpty()) {
                sendSimpleMessage(email.getEmail());
                return new EmailResponseDto(email.getEmail());
            }

            if (emailService.findByEmail(email.getEmail()).getJoined() == true) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }
            sendSimpleMessage(email.getEmail());
            return new EmailResponseDto(email.getEmail());
        }
        else {
            throw new IllegalArgumentException("형식에 맞지 않는 이메일입니다.");
        }
    }

    @ApiOperation(value = "비밀번호 재설정을 위한 이메일 발송", notes = "가입된 이메일의 경우이며 비밀번호를 잊었을 때 사용합니다. 학교 이메일 형식(@inu.ac.kr)으로 이메일을 받아서 해당 이메일로 인증코드를 발송합니다.")
    @PostMapping("/email/password")
    public EmailResponseDto findPassword(@RequestBody @Valid EmailRequest email) throws Exception {

        if(emailService.findByEmail(email.getEmail()).getJoined()==true) {
            sendSimpleMessage(email.getEmail());
        }
        else {
            throw new IllegalArgumentException("가입하지 않은 이메일입니다.");
        }
        return new EmailResponseDto(email.getEmail());
    }

    @ApiOperation(value = "비밀번호 재설정을 위한 이메일 인증 코드 검증", notes = "/email/password 에서 발송한 인증코드를 확인할 때 사용합니다.")
    @PostMapping("/verifyCode/password/{email}")
    public String verifyCodePassword(@PathVariable("email")String email, @RequestBody VerifyRequest code) {
        if(emailService.findByEmail(email).getCode().equals(code.getCode())) {
            Member mem = memberService.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
            Iterator<String> iter =mem.getRoles().iterator();
            List<String> roles=new ArrayList<>();
            while (iter.hasNext()) {
                roles.add(iter.next());
            }
            return jwtTokenProvider.createToken(mem.getUsername(), roles);
        }
        else{
            throw new IllegalArgumentException("잘못된 인증번호입니다.");
        }
    }

    @ApiOperation(value = "이메일 인증 코드 검증", notes = "/email에서 발송한 인증코드를 확인할 때 사용합니다.")
    @PostMapping("/verifyCode/{email}")
    public EmailResponseDto verifyCode(@PathVariable("email")String email, @RequestBody VerifyRequest code) {
        if(emailService.findByEmail(email).getCode().equals(code.getCode())) {
            emailService.isChecked(email);
            return new EmailResponseDto(code.getCode());
        }
        else{
            throw new IllegalArgumentException("잘못된 인증번호입니다.");
        }
    }


    /**
     *함수
     */

    //이메일,인증번호로그/DB 저장
    private MimeMessage createMessage(String to)throws Exception{
        logger.info("보내는 대상 : "+ to);

        MimeMessage  message = emailSender.createMimeMessage();
        String ePw = createKey();
        logger.info("인증 번호 : " + ePw);
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
    public void sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);

        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException("없는 이메일 주소 입니다");
        }
    }


}