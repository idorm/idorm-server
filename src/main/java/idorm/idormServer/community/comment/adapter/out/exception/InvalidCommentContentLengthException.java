package idorm.idormServer.community.comment.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.comment.adapter.out.CommentResponseCode;

public class InvalidCommentContentLengthException extends BaseException {

	public InvalidCommentContentLengthException() {
		super(CommentResponseCode.INVALID_CONTENT_LENGTH);
	}
}
