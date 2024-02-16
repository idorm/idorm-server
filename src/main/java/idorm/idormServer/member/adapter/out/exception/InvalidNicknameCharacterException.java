package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class InvalidNicknameCharacterException extends BaseException {

  public InvalidNicknameCharacterException() {
    super(MemberResponseCode.INVALID_NICKNAME_CHARACTER);
  }
}
