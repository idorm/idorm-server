package idorm.idormServer.auth.adapter.out.exception;

import idorm.idormServer.auth.adapter.out.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedAccessTokenException extends BaseException {

	public UnAuthorizedAccessTokenException() {
		super(AuthResponseCode.UNAUTHORIZED_ACCESS_TOKEN);
	}
}
