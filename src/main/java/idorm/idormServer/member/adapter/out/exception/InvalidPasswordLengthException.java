package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class InvalidPasswordLengthException extends BaseException {

  public InvalidPasswordLengthException() {
    super(MemberResponseCode.INVALID_PASSWORD_LENGTH);
  }
}
