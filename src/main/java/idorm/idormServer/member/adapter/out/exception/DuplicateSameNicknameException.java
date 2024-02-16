package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class DuplicateSameNicknameException extends BaseException {

  public DuplicateSameNicknameException() {
    super(MemberResponseCode.DUPLICATED_SAME_NICKNAME);
  }

}
