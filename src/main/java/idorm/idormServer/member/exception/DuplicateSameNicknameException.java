package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicateSameNicknameException extends BaseException {

  public DuplicateSameNicknameException() {
    super(MemberResponseCode.DUPLICATED_SAME_NICKNAME);
  }

}
