package idorm.idormServer.community.post.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.post.adapter.out.PostResponseCode;

public class AccessDeniedPostException extends BaseException {

  public AccessDeniedPostException() {
    super(PostResponseCode.ACCESS_DENIED_POST);
  }
}
