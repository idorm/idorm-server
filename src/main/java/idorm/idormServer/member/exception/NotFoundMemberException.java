package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundMemberException extends BaseException {

  public NotFoundMemberException() {
    super(MemberResponseCode.NOT_FOUND_MEMBER);
  }
}