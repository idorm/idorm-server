package idorm.idormServer.email.application.port.out;

import idorm.idormServer.email.domain.VerificationCode;

public interface GenerateVerificationCodePort {

    VerificationCode generate();
}
