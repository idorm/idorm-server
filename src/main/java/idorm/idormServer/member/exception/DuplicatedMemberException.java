package idorm.idormServer.member.exception;


import idorm.idormServer.common.exception.BaseException;

public class DuplicatedMemberException extends BaseException {

  public DuplicatedMemberException() {
    super(MemberResponseCode.DUPLICATED_MEMBER);
  }
}