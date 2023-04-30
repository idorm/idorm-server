package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.dto.EmailAuthRequestDto;
import idorm.idormServer.email.dto.EmailVerifyRequestDto;
import idorm.idormServer.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceFacade {

    private EmailService emailService;

    public void sendAuthEmail(Optional<Email> existingEmail,
                               EmailAuthRequestDto request,
                               String verificationCode) {

        if (existingEmail.isPresent()) {

            if (existingEmail.get().getIsCheck()) {
                if (existingEmail.get().getMember() != null) {
                    if (!existingEmail.get().getMember().getIsDeleted()) {
                        throw new CustomException(null, DUPLICATE_EMAIL);
                    } else {
                        emailService.delete(existingEmail.get());
                    }
                } else {
                    emailService.delete(existingEmail.get());
                }
            } else {
                emailService.delete(existingEmail.get());
            }
        }

        Email email = emailService.save(request.toEntity(verificationCode));
        emailService.sendVerificationEmail(email);
    }

    public void verifyCode(Optional<Email> existingEmail, EmailVerifyRequestDto request) {
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
    }

    public void findPassword(Email foundEmail) {
        String verificationCode = emailService.createVerificationCode();
        emailService.updateVerificationCode(foundEmail, verificationCode);

        emailService.sendVerificationEmail(foundEmail);
        emailService.updateIsPossibleUpdatePassword(foundEmail, false);
    }
}
