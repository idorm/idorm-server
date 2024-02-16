package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class NotFoundMemberException extends BaseException {

  public NotFoundMemberException() {
    super(MemberResponseCode.NOT_FOUND_MEMBER);
  }
}