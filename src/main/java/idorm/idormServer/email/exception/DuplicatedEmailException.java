package idorm.idormServer.email.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedEmailException extends BaseException {

  public DuplicatedEmailException() {
    super(EmailResponseCode.DUPLICATED_EMAIL);
  }
}
