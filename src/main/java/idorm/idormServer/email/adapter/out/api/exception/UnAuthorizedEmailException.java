package idorm.idormServer.email.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;

public class UnAuthorizedEmailException extends BaseException {

  public UnAuthorizedEmailException() {
    super(EmailResponseCode.UNAUTHORIZED_EMAIL);
  }
}
