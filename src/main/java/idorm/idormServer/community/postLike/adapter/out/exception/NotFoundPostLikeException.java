package idorm.idormServer.community.postLike.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.postLike.adapter.out.PostLikeResponseCode;

public class NotFoundPostLikeException extends BaseException {

	public NotFoundPostLikeException() {
		super(PostLikeResponseCode.NOT_FOUND_POSTLIKE);
	}
}
