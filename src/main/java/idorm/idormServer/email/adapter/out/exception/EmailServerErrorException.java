package idorm.idormServer.email.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.EmailResponseCode;

public class EmailServerErrorException extends BaseException {

  public EmailServerErrorException() {
    super(EmailResponseCode.EMAIL_SERVER_ERROR);
  }
}
