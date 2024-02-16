package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class InvalidNicknameLengthException extends BaseException {

  public InvalidNicknameLengthException() {
    super(MemberResponseCode.INVALID_NICKNAME_LENGTH);
  }
}
