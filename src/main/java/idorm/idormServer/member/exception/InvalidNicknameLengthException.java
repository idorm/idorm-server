package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidNicknameLengthException extends BaseException {

  public InvalidNicknameLengthException() {
    super(MemberResponseCode.INVALID_NICKNAME_LENGTH);
  }
}
