package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedLikedException extends BaseException {

  public DuplicatedLikedException() {
    super(CommunityResponseCode.DUPLICATED_LIKED);
  }
}
