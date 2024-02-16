package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundPostPhotoException extends BaseException {

  public NotFoundPostPhotoException() {
    super(CommunityResponseCode.NOT_FOUND_POSTPHOTO);
  }
}
