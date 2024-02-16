package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class InvalidPasswordCharacterException extends BaseException {

  public InvalidPasswordCharacterException() {
    super(MemberResponseCode.INVALID_PASSWORD_CHARACTER);
  }
}
