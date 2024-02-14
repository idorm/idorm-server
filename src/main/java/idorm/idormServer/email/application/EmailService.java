package idorm.idormServer.email.application;

import idorm.idormServer.email.application.port.in.EmailUseCase;
import idorm.idormServer.email.application.port.out.LoadEmailPort;
import idorm.idormServer.email.application.port.out.SaveEmailPort;
import idorm.idormServer.email.application.port.out.GenerateVerificationCodePort;
import idorm.idormServer.email.application.port.out.SendEmailPort;
import idorm.idormServer.email.domain.VerificationCode;
import idorm.idormServer.email.application.port.in.dto.EmailVerifyRequest;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.application.port.in.dto.EmailSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static idorm.idormServer.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailUseCase {

    private final SaveEmailPort saveEmailPort;
    private final LoadEmailPort loadEmailPort;
    private final GenerateVerificationCodePort verificationCodePort;
    private final SendEmailPort sendEmailPort;

    @Override
    public void sendVerificationEmail(EmailSendRequest request) {
        Email email = loadEmailPort.findByEmail(request.email());

        if (email != null) {
            email.updateVerificationCode(generateVerificationCode());
        } else {
            email = new Email(request.email(), generateVerificationCode());
        }
        saveEmailPort.save(email);
        sendEmailPort.send(email);
    }

    private VerificationCode generateVerificationCode() {
        return verificationCodePort.generate();
    }

    @Override
    public void sendReverificationEmail(EmailSendRequest request) {
        Email email = loadEmailPort.findByEmail(request.email());

        if (email == null) {
            throw new CustomException(null, EMAIL_NOT_FOUND);
        } else {
            email.updateReVerificationCode(generateVerificationCode());
        }
        saveEmailPort.save(email);
        sendEmailPort.send(email);
    }

    @Override
    @Transactional
    public void verifyCode(EmailVerifyRequest request) {
        Email email = loadEmailPort.findByEmail(request.email());

        if (email == null) {
            throw new CustomException(null, EMAIL_NOT_FOUND);
        }
        email.verify(request.code());
    }

    @Transactional
    public void reVerifyCode(EmailVerifyRequest request) {
        Email email = loadEmailPort.findByEmail(request.email());

        if (email == null) {
            throw new CustomException(null, EMAIL_NOT_FOUND);
        }
        email.reVerify(request.code());
    }

    // TODO: Member 리팩 시 수정 코드
//    public void validateNewMember(final String email) {
//        Email mail = emailRepository.findByEmailAndEmailStatus(email, EmailStatus.VERIFIED)
//                .orElseThrow(() -> new CustomException(null, UNAUTHORIZED_EMAIL));
//
//        mail.validateValidTime(LocalDateTime.now());
//        validateNotSignUpEmail(mail);
//    }
//
//    public void validateUpdatePassword(final String email) {
//        Email mail = emailRepository.findByEmailAndEmailStatus(email, EmailStatus.RE_VERIFIED)
//                .orElseThrow(() -> new CustomException(null, UNAUTHORIZED_EMAIL));
//
//        mail.validateValidTime(LocalDateTime.now());
//        validateSignUpEmail(mail);
//    }
//
//    @Transactional
//    public void useEmail(final String email) {
//        Email mail = emailRepository.findByEmail(email)
//                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));
//        mail.register();
//    }
}