package idorm.idormServer.community.comment.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.comment.adapter.out.CommentResponseCode;

public class AlreadyDeletedCommentException extends BaseException {

	public AlreadyDeletedCommentException() {
		super(CommentResponseCode.ALREADY_DELETED_COMMENT);
	}
}
