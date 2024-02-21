package idorm.idormServer.auth.adapter.out.api.exception;

import idorm.idormServer.auth.adapter.out.api.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedAccessTokenException extends BaseException {

	public UnAuthorizedAccessTokenException() {
		super(AuthResponseCode.UNAUTHORIZED_ACCESS_TOKEN);
	}
}
