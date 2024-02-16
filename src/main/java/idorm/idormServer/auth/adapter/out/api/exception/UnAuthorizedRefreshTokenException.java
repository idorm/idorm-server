package idorm.idormServer.auth.adapter.out.api.exception;

import idorm.idormServer.auth.adapter.out.api.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class UnAuthorizedRefreshTokenException extends BaseException {

	public UnAuthorizedRefreshTokenException() {
		super(AuthResponseCode.UNAUTHORIZED_REFRESH_TOKEN);
	}
}