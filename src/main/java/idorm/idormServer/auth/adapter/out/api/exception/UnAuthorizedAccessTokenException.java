package idorm.idormServer.auth.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class UnAuthorizedAccessTokenException extends BaseException {

  public UnAuthorizedAccessTokenException() {
    super(MemberResponseCode.INVALID_ACCESS_TOKEN);
  }
}
