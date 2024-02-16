package idorm.idormServer.email.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;

public class NotFoundEmailException extends BaseException {

  public NotFoundEmailException() {
    super(EmailResponseCode.NOT_FOUND_EMAIL);
  }
}
