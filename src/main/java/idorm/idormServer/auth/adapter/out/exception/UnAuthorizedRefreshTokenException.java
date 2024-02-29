package idorm.idormServer.auth.adapter.out.exception;

import idorm.idormServer.auth.adapter.out.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedRefreshTokenException extends BaseException {

	public UnAuthorizedRefreshTokenException() {
		super(AuthResponseCode.UNAUTHORIZED_REFRESH_TOKEN);
	}
}