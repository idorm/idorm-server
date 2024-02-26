package idorm.idormServer.community.post.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.post.adapter.out.PostResponseCode;

public class InvalidPostContentLengthException extends BaseException {

	public InvalidPostContentLengthException() {
		super(PostResponseCode.INVALID_CONTENT_LENGTH);
	}
}
