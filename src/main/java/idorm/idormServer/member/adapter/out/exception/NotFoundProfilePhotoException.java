package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class NotFoundProfilePhotoException extends BaseException {

  public NotFoundProfilePhotoException() {
    super(MemberResponseCode.NOT_FOUND_PROFILE_PHOTO);
  }
}