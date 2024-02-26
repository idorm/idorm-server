package idorm.idormServer.community.comment.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.comment.adapter.out.CommentResponseCode;

public class AccessDeniedUpdateCommentException extends BaseException {

	public AccessDeniedUpdateCommentException() {
		super(CommentResponseCode.ACCESS_DENIED_UPDATE_COMMENT);
	}
}
