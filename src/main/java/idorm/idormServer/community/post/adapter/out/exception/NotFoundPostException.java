package idorm.idormServer.community.post.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.post.adapter.out.PostResponseCode;

public class NotFoundPostException extends BaseException {

	public NotFoundPostException() {
		super(PostResponseCode.NOT_FOUND_POST);
	}
}
