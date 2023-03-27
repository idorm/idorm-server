package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.repository.EmailRepository;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;

    /**
     * DB에 이메일 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Email save(Email email) {

        try {
            return emailRepository.save(email);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 이메일 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Email email){

        try {
            email.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 이메일 가입 여부 변경 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateIsJoined(Email email, Member member) {

        try {
            email.isJoined(member);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 이메일 인증 코드 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateVerificationCode(Email email, String code) {

        try {
            email.updateCode(code);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 비밀번호 변경 가능 여부 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateIsPossibleUpdatePassword(Email email, Boolean isPossibleUpdatePassword) {
        try {
            if (isPossibleUpdatePassword)
                updateIsPossibleUpdatePasswordCreatedAt(email);

            email.updateIsPossibleUpdatePassword(isPossibleUpdatePassword);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 비밀번호 변경 가능 시 유효 시작 시간 수정 |
     */
    @Transactional
    public void updateIsPossibleUpdatePasswordCreatedAt(Email email) {
        try {
            email.updateIsPossibleUpdatePasswordCreatedAt();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 이메일 단건 조회 |
     * 404(EMAIL_NOT_FOUND)
     */
    public Email findByEmail(String email) {

        return emailRepository.findByEmailAndIsDeletedIsFalse(email)
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));
    }

    /**
     * 이메일로 회원 조회 |
     * 404(MEMBER_NOT_FOUND)
     */
    public Optional<Email> findMemberByEmail(String email) {

        try {
            return emailRepository.findByEmailAndIsCheckIsTrueAndIsDeletedIsFalseAndMemberIsNotNull(email);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 가입한 이메일 존재 여부 검증 |
     * 409(DUPLICATE_EMAIL)
     * 500(SERVER_ERROR)
     */
    public void isExistingRegisteredEmail(String email) {
        Optional<Email> registeredEmail = null;

        try {
            registeredEmail = emailRepository.findByEmailAndIsCheckIsTrueAndIsDeletedIsFalseAndMemberIsNotNull(email);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        if (registeredEmail.isPresent())
            throw new CustomException(null, DUPLICATE_EMAIL);
    }

    /**
     * 이메일 존재 여부 검증 |
     * 500(SERVER_ERROR)
     */
    public Optional<Email> isExistingEmail(String email) {
        try {
            return emailRepository.findByEmailAndIsDeletedIsFalse(email);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 이메일 형식 검증 |
     * 400(EMAIL_CHARACTER_INVALID)
     */
    public void validateEmailDomain(String email) {

        String[] mailSplit = email.split("@");

        if(!(mailSplit.length == 2) || !mailSplit[1].equals("inu.ac.kr")) {
            throw new CustomException(null,EMAIL_CHARACTER_INVALID);
        }
    }

    /**
     * 이메일 유효 시간 검증 |
     * 401(EXPIRED_CODE)
     */
    public void validateEmailIsExpired(LocalDateTime createdAt) {

        if (LocalDateTime.now().isAfter(createdAt.plusMinutes(5)))
            throw new CustomException(null, EXPIRED_CODE);
    }

    /**
     * 이메일 인증 코드 검증 |
     * 400(INVALID_CODE)
     */
    @Transactional
    public void validateEmailCodeIsValid(Email email, String verificationCode) {

        if (email.getCode().equals(verificationCode))
            email.isChecked();
        else
            throw new CustomException(null, INVALID_CODE);
    }

    /**
     * 비밀번호 변경 가능 여부 검증 |
     * 401(UNAUTHORIZED_EMAIL)
     * 401(EXPIRED_CODE)
     */
    public void validateIsPossibleUpdatePassword(Email email) {

        if (!email.getIsPossibleUpdatePassword())
            throw new CustomException(null, UNAUTHORIZED_EMAIL);

        if (LocalDateTime.now().isAfter(email.getIsPossibleUpdatePasswordCreatedAt().plusMinutes(5)))
            throw new CustomException(null, EXPIRED_CODE);
    }

    /**
     * 인증 메일 전송 |
     * 500(EMAIL_SENDING_ERROR)
     */
    public void sendVerificationEmail(Email email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            mimeMessage.addRecipients(MimeMessage.RecipientType.TO, email.getEmail());
            mimeMessage.setSubject("[idorm] 인증 코드: " + email.getCode(), "utf-8");

            String emailContent = createEmailContent(email.getCode());

            mimeMessage.setText(emailContent, "utf-8", "html");
            mimeMessage.setFrom("idormServer@gmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException(e, EMAIL_SENDING_ERROR);
        }
    }

    /**
     * 이메일 인증 코드 생성 |
     */
    public String createVerificationCode() {

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        for (int i = 0; i < 6; i++) {
            stringBuilder.append((random.nextInt(10)));
        }
        String code = stringBuilder.toString();

        return code.substring(0, 3) + "-" + code.substring(3, 6);
    }

    /**
     * 이메일 내용 생성 |
     */
    private String createEmailContent(String verificationCode) {

        String message = "";
        message += "<img width=\"120\" height=\"36\" style=\"margin-top: 0; margin-right: 0; margin-bottom: 32px; margin-left: 0px; padding-right: 30px; padding-left: 30px;\" src=\"https://slack.com/x-a1607371436052/img/slack_logo_240.png\" alt=\"\" loading=\"lazy\">";
        message += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        message += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 Slack 가입 창이 있는 브라우저 창에 입력하세요.</p>";
        message += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        message += verificationCode;
        message += "</td></tr></tbody></table></div>";
        message += "<a href=\"https://slack.com\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">Slack Clone Technologies, Inc</a>";

        return message;
    }
}