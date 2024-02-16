package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedDeleteMemberExceptioin extends BaseException {

  public UnAuthorizedDeleteMemberExceptioin() {
    super(MemberResponseCode.UNAUTHORIZED_DELETED_MEMBER);
  }
}
