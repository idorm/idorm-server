package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class NotFoundPostPhotoException extends BaseException {

  public NotFoundPostPhotoException() {
    super(CommunityResponseCode.NOT_FOUND_POSTPHOTO);
  }
}
