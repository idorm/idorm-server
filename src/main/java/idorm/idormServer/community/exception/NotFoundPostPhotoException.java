package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class NotFoundPostPhotoException extends BaseException {

  public NotFoundPostPhotoException() {
    super(CommunityResponseCode.NOT_FOUND_POSTPHOTO);
  }
}
