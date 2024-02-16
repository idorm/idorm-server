package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidNicknameCharacterException extends BaseException {

  public InvalidNicknameCharacterException() {
    super(MemberResponseCode.INVALID_NICKNAME_CHARACTER);
  }
}
