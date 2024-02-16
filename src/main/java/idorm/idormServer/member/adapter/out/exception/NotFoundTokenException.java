package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.auth.adapter.out.api.AuthResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class NotFoundTokenException extends BaseException {

	public NotFoundTokenException() {
		super(AuthResponseCode.NOT_FOUND_TOKEN);
	}
}
