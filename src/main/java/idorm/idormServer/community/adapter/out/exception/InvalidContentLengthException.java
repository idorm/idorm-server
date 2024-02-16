package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class InvalidContentLengthException extends BaseException {

  public InvalidContentLengthException() {
    super(CommunityResponseCode.INVALID_CONTENT_LENGTH);
  }
}
