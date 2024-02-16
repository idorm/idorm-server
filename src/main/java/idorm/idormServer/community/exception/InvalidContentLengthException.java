package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidContentLengthException extends BaseException {

  public InvalidContentLengthException() {
    super(CommunityResponseCode.INVALID_CONTENT_LENGTH);
  }
}
