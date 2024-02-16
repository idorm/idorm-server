package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidAccessTokenException extends BaseException {

  public InvalidAccessTokenException() {
    super(MemberResponseCode.INVALID_ACCESS_TOKEN);
  }
}
