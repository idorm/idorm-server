package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.EmailStatus;
import idorm.idormServer.email.domain.VerificationCode;
import idorm.idormServer.email.dto.EmailVerifyRequest;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.dto.EmailSendRequest;
import idorm.idormServer.email.repository.EmailRepository;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static idorm.idormServer.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;
    private final MailClient mailClient;
    private final VerificationCodeProvider verificationCodeProvider;
    private final Clock clock;

    @Value("${s3.logo}")
    private String s3LogoImgUrl;

    @Transactional
    public void sendVerificationMail(EmailSendRequest request) {
        Optional<Email> email = emailRepository.findByEmail(request.email());

        if (email.isPresent()) {
            validateNotSignUpEmail(email.get());

            email.get().updateCode(verificationCodeProvider.provide(), LocalDateTime.now(clock));
            sendEmail(email.get());
            return;
        }

        sendEmail(new Email(request.email(), verificationCodeProvider.provide(), LocalDateTime.now(clock)));
    }

    @Transactional
    public void sendRegisteredMail(EmailSendRequest request) {
        Email email = emailRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));

        validateSignUpEmail(email);

        email.updateCode(verificationCodeProvider.provide(), LocalDateTime.now(clock));
        sendEmail(email);
    }

    @Transactional
    public void verifyCode(String email, EmailVerifyRequest request) {
        Email mail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));

        validateNotSignUpEmail(mail);
        mail.verify(request.code(), LocalDateTime.now(clock));
    }

    @Transactional
    public void reVerifyCode(String email, EmailVerifyRequest request) {
        Email mail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));

        validateSignUpEmail(mail);
        mail.verify(request.code(), LocalDateTime.now(clock));
    }

    private void validateNotSignUpEmail(Email email) {
        if (email.isRegistered()) {
            throw new CustomException(null, DUPLICATE_EMAIL);
        }
    }

    private void validateSignUpEmail(Email email) {
        if (!email.isRegistered()) {
            throw new CustomException(null, MEMBER_NOT_FOUND);
        }
    }

    public void validateNewMember(final String email) {
        Email mail = emailRepository.findByEmailAndEmailStatus(email, EmailStatus.VERIFIED)
                .orElseThrow(() -> new CustomException(null, UNAUTHORIZED_EMAIL));

        mail.verifyRegisterTime(LocalDateTime.now(clock));
        validateNotSignUpEmail(mail);
    }

    public void validateUpdatePassword(final String email) {
        Email mail = emailRepository.findByEmailAndEmailStatus(email, EmailStatus.RE_VERIFIED)
                .orElseThrow(() -> new CustomException(null, UNAUTHORIZED_EMAIL));

        mail.verifyRegisterTime(LocalDateTime.now(clock));
        validateSignUpEmail(mail);
    }

    @Transactional
    public void useEmail(final String email) {
        Email mail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));
        mail.register();
    }

    private void sendEmail(Email email) {
        mailClient.send(mail -> {
            mail.setTo(email.getEmail());
            mail.setSubject("[idorm] 인증 코드: " + email.getCode());
            mail.setText(createEmailContent(email.getCode()));
        });
    }

    private String createEmailContent(VerificationCode verificationCode) {

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
        template = template.replace("${verificationCode}", verificationCode.getCode());
        template = template.replace("${logoUrl}", s3LogoImgUrl);
        return template;
    }
}