package idorm.idormServer.auth.adapter.out.api.exception;

import idorm.idormServer.auth.adapter.out.api.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedAdminException extends BaseException {

	public AccessDeniedAdminException() {
		super(AuthResponseCode.ACCESS_DENIED_ADMIN);
	}
}