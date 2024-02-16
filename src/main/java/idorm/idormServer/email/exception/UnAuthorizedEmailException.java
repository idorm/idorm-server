package idorm.idormServer.email.exception;

import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedEmailException extends BaseException {

  public UnAuthorizedEmailException() {
    super(EmailResponseCode.UNAUTHORIZED_EMAIL);
  }
}
