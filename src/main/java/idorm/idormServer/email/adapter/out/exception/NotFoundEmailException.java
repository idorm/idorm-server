package idorm.idormServer.email.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.EmailResponseCode;

public class NotFoundEmailException extends BaseException {

  public NotFoundEmailException() {
    super(EmailResponseCode.NOT_FOUND_EMAIL);
  }
}
