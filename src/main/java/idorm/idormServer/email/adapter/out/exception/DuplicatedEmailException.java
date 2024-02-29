package idorm.idormServer.email.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.EmailResponseCode;

public class DuplicatedEmailException extends BaseException {

  public DuplicatedEmailException() {
    super(EmailResponseCode.DUPLICATED_EMAIL);
  }
}
