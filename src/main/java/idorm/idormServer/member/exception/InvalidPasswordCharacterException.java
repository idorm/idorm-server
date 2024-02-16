package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidPasswordCharacterException extends BaseException {

  public InvalidPasswordCharacterException() {
    super(MemberResponseCode.INVALID_PASSWORD_CHARACTER);
  }
}
