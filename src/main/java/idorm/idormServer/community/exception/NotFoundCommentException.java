package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundCommentException extends BaseException {

	public NotFoundCommentException() {
		super(CommunityResponseCode.NOT_FOUND_COMMENT);
	}
}
