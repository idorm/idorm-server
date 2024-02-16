package idorm.idormServer.email.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;

public class InvalidEmailCharacterException extends BaseException {

  public InvalidEmailCharacterException() {
    super(EmailResponseCode.INVALID_EMAIL_CHARACTER);
  }
}
