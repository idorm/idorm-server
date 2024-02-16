package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidRefreshTokenException extends BaseException {

  public InvalidRefreshTokenException() {
    super(MemberResponseCode.INVALID_REFRESH_TOKEN);
  }
}
