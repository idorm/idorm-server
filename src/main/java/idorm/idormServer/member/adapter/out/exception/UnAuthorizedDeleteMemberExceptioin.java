package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class UnAuthorizedDeleteMemberExceptioin extends BaseException {

  public UnAuthorizedDeleteMemberExceptioin() {
    super(MemberResponseCode.UNAUTHORIZED_DELETED_MEMBER);
  }
}
