package idorm.idormServer.member.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Email;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.mail.username}")
    private String adminMail;

    @Value("${s3.logo}")
    private String s3LogoImgUrl;

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
    public void delete(Email email) {

        try {
            email.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 이메일 데이터 삭제 |
     * 회원 탈퇴 시 사용한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteData(Email email) {

        try {
            email.deleteData();
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

        if (!(mailSplit.length == 2) || !mailSplit[1].equals("inu.ac.kr")) {
            throw new CustomException(null, EMAIL_CHARACTER_INVALID);
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
     * 이메일 존재 여부 검증 |
     * 409(DUPLICATE_EMAIL)
     */
    public boolean validateEmailExistence(Optional<Email> email) {
        if (email.isPresent()) {

            if (email.get().getIsCheck()) {
                if (email.get().getMember() != null) {
                    if (!email.get().getMember().getIsDeleted()) {
                        throw new CustomException(null, DUPLICATE_EMAIL);
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 인증 메일 전송 |
     * 500(EMAIL_SERVER_ERROR)
     */
    public void sendVerificationEmail(Email email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            mimeMessage.addRecipients(MimeMessage.RecipientType.TO, email.getEmail());
            mimeMessage.setSubject("[idorm] 인증 코드: " + email.getCode(), "utf-8");

            String emailContent = createEmailContent(email.getCode());

            mimeMessage.setText(emailContent, "utf-8", "html");
            mimeMessage.setFrom(adminMail);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException(e, EMAIL_SERVER_ERROR);
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

        String template = "<head> " +
                "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\" /> " +
                "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin /> " +
                "<link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap\" rel=\"stylesheet\" />" +
                "</head>" +
                "<body style=\" background-color: #f2f5fa; display: flex; justify-content: center; align-items: center; font-family: 'Noto Sans KR', sans-serif; \"> " +
                "<div style=\"width: 400px; background-color: #ffffff; padding: 70px 30px\"> <img src=\"${logoUrl}\" width=\"70\" /> " +
                "<p style=\" font-size: 25px; font-weight: 500; margin-top: 60px; color: #5b5b5b; \" > " +
                "<span>요청하신 </span> <span style=\"color: #71a1fe; font-weight: 700\">인증번호</span> <br />발송해드립니다. </p> " +
                "<p style=\"font-weight: 400; font-size: 20px\"> 아래 인증번호를 입력창에 입력해 주세요. </p> " +
                "<p style=\" border: 1px #e3e1ec; border-top-style: solid; padding: 25px; background-color: #f2f5fa; \" > 인증번호 <span style=\"color: #ff6868\">${verificationCode}</span> </p> </div>" +
                "</body>";
        template = template.replace("${verificationCode}", verificationCode);
        template = template.replace("${logoUrl}", s3LogoImgUrl);
        return template;
    }
}