package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class UnAuthorizedPasswordException extends BaseException {

  public UnAuthorizedPasswordException() {
    super(MemberResponseCode.UNAUTHORIZED_PASSWORD);
  }
}
