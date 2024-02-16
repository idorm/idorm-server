package idorm.idormServer.email.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundEmailException extends BaseException {

  public NotFoundEmailException() {
    super(EmailResponseCode.NOT_FOUND_EMAIL);
  }
}
