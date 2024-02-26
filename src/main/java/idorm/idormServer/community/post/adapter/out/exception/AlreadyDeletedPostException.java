package idorm.idormServer.community.post.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.post.adapter.out.PostResponseCode;

public class AlreadyDeletedPostException extends BaseException {

	public AlreadyDeletedPostException() {
		super(PostResponseCode.ALREADY_DELETED_POST);
	}
}
