package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedNicknameException extends BaseException {

  public DuplicatedNicknameException() {
    super(MemberResponseCode.DUPLICATED_NICKNAME);
  }

}
