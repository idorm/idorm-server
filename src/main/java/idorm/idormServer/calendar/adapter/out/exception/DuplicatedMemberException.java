package idorm.idormServer.calendar.adapter.out.exception;


import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class DuplicatedMemberException extends BaseException {

  public DuplicatedMemberException() {
    super(MemberResponseCode.DUPLICATED_MEMBER);
  }
}