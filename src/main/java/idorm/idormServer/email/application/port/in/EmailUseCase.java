package idorm.idormServer.email.application.port.in;

import idorm.idormServer.email.application.port.in.dto.EmailSendRequest;
import idorm.idormServer.email.application.port.in.dto.EmailVerifyRequest;

public interface EmailUseCase {

    void sendVerificationEmail(EmailSendRequest request);

    void sendReverificationEmail(EmailSendRequest request);

    void verifyCode(EmailVerifyRequest request);

    void reVerifyCode(EmailVerifyRequest request);
}