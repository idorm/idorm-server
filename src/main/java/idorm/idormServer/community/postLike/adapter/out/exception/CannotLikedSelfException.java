package idorm.idormServer.community.postLike.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.postLike.adapter.out.PostLikeResponseCode;

public class CannotLikedSelfException extends BaseException {

	public CannotLikedSelfException() {
		super(PostLikeResponseCode.CANNOT_LIKED_SELF);
	}
}
