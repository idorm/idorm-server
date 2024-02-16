package idorm.idormServer.email.exception;

import idorm.idormServer.common.exception.BaseException;

public class EmailServerErrorException extends BaseException {

  public EmailServerErrorException() {
    super(EmailResponseCode.EMAIL_SERVER_ERROR);
  }
}
