package idorm.idormServer.community.postLike.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.postLike.adapter.out.PostLikeResponseCode;

public class CannotLikedPostByDeletedMemberException extends BaseException {

	public CannotLikedPostByDeletedMemberException() {
		super(PostLikeResponseCode.CANNOT_LIKED_POST_BY_DELETED_MEMBER);
	}
}
