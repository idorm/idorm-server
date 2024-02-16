package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class CannotUpdateNicknameException extends BaseException {

  public CannotUpdateNicknameException() {
    super(MemberResponseCode.CANNOT_UPDATE_NICKNAME);
  }
}
