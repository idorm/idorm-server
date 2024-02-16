package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidPasswordLengthException extends BaseException {

  public InvalidPasswordLengthException() {
    super(MemberResponseCode.INVALID_PASSWORD_LENGTH);
  }
}
