package idorm.idormServer.email.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;

public class InvalidVerificationCode extends BaseException {

	public InvalidVerificationCode() {
		super(EmailResponseCode.INVALID_VERIFICATION_CODE);
	}
}
