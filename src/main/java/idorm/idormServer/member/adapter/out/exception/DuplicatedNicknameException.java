package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class DuplicatedNicknameException extends BaseException {

  public DuplicatedNicknameException() {
    super(MemberResponseCode.DUPLICATED_NICKNAME);
  }

}
