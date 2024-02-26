package idorm.idormServer.community.post.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.post.adapter.out.PostResponseCode;

public class InvalidPostTitleLengthException extends BaseException {

	public InvalidPostTitleLengthException() {
		super(PostResponseCode.INVALID_TITLE_LENGTH);
	}
}
