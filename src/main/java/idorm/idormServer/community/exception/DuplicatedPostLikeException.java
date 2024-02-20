package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedPostLikeException extends BaseException {

	public DuplicatedPostLikeException() {
		super(CommunityResponseCode.DUPLICATED_POST_LIKE);
	}
}
