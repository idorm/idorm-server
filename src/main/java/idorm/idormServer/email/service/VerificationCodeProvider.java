package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.VerificationCode;

public interface VerificationCodeProvider {
    VerificationCode provide();
}
