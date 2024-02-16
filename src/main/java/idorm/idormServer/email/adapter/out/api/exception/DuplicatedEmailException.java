package idorm.idormServer.email.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;

public class DuplicatedEmailException extends BaseException {

  public DuplicatedEmailException() {
    super(EmailResponseCode.DUPLICATED_EMAIL);
  }
}
