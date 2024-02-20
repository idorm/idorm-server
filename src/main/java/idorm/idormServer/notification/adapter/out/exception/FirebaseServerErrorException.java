package idorm.idormServer.notification.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.notification.adapter.out.FcmResponseCode;

public class FirebaseServerErrorException extends BaseException {

	public FirebaseServerErrorException() {
		super(FcmResponseCode.FIREBASE_SERER_ERROR);
	}
}
