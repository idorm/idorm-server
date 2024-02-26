package idorm.idormServer.community.post.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.post.adapter.out.PostResponseCode;

public class AccessDeniedUpdatePostException extends BaseException {

	public AccessDeniedUpdatePostException() {
		super(PostResponseCode.ACCESS_DENIED_UPDATE_POST);
	}
}
