package idorm.idormServer.member.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundProfilePhotoException extends BaseException {

  public NotFoundProfilePhotoException() {
    super(MemberResponseCode.NOT_FOUND_PROFILE_PHOTO);
  }
}