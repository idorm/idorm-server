package idorm.idormServer.community.postLike.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.postLike.adapter.out.PostLikeResponseCode;

public class DuplicatedPostLikeException extends BaseException {

	public DuplicatedPostLikeException() {
		super(PostLikeResponseCode.DUPLICATED_POST_LIKE);
	}
}
