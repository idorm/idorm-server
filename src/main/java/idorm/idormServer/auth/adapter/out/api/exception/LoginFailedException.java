package idorm.idormServer.auth.adapter.out.api.exception;

import idorm.idormServer.auth.adapter.out.api.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class LoginFailedException extends BaseException {

	public LoginFailedException() {
		super(AuthResponseCode.UNAUTHORIZED_LOGIN_INFO);
	}
}