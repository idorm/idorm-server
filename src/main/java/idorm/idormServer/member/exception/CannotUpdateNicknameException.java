package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class CannotUpdateNicknameException extends BaseException {

  public CannotUpdateNicknameException() {
    super(MemberResponseCode.CANNOT_UPDATE_NICKNAME);
  }
}
