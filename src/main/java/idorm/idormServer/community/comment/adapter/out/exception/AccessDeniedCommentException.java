package idorm.idormServer.community.comment.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.comment.adapter.out.CommentResponseCode;

public class AccessDeniedCommentException extends BaseException {

	public AccessDeniedCommentException() {
		super(CommentResponseCode.ACCESS_DENIED_COMMENT);
	}
}
