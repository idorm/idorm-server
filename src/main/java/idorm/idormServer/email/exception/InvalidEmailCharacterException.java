package idorm.idormServer.email.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidEmailCharacterException extends BaseException {

  public InvalidEmailCharacterException() {
    super(EmailResponseCode.INVALID_EMAIL_CHARACTER);
  }
}
