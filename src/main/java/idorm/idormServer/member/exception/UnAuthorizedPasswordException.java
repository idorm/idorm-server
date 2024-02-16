package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedPasswordException extends BaseException {

  public UnAuthorizedPasswordException() {
    super(MemberResponseCode.UNAUTHORIZED_PASSWORD);
  }
}
