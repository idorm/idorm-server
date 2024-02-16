package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class DuplicatedLikedException extends BaseException {

  public DuplicatedLikedException() {
    super(CommunityResponseCode.DUPLICATED_LIKED);
  }
}
