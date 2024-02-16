package idorm.idormServer.email.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;

public class EmailServerErrorException extends BaseException {

  public EmailServerErrorException() {
    super(EmailResponseCode.EMAIL_SERVER_ERROR);
  }
}
