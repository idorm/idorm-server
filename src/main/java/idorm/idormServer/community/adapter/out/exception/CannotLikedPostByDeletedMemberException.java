package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class CannotLikedPostByDeletedMemberException extends BaseException {

  public CannotLikedPostByDeletedMemberException() {
    super(CommunityResponseCode.CANNOT_LIKED_POST_BY_DELETED_MEMBER);
  }
}
