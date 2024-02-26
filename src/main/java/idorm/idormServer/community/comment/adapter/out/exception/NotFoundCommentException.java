package idorm.idormServer.community.comment.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.comment.adapter.out.CommentResponseCode;

public class NotFoundCommentException extends BaseException {

	public NotFoundCommentException() {
		super(CommentResponseCode.NOT_FOUND_COMMENT);
	}
}
