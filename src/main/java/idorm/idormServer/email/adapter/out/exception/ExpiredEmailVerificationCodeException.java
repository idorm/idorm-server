package idorm.idormServer.email.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.EmailResponseCode;

public class ExpiredEmailVerificationCodeException extends BaseException {

  public ExpiredEmailVerificationCodeException() {
    super(EmailResponseCode.EXPIRED_VERIFICATION_CODE);
  }
}
