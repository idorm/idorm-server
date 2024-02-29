package idorm.idormServer.email.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.EmailResponseCode;

public class InvalidEmailCharacterException extends BaseException {

  public InvalidEmailCharacterException() {
    super(EmailResponseCode.INVALID_EMAIL_CHARACTER);
  }
}
