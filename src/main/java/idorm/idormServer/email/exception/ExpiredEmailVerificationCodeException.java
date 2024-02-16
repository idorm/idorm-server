package idorm.idormServer.email.exception;

import idorm.idormServer.common.exception.BaseException;

public class ExpiredEmailVerificationCodeException extends BaseException {

  public ExpiredEmailVerificationCodeException() {
    super(EmailResponseCode.EXPIRED_EMAIL_VERIFICATION_CODE);
  }
}
