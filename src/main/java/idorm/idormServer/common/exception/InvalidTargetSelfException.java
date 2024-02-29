package idorm.idormServer.common.exception;

public class InvalidTargetSelfException extends BaseException {

	public InvalidTargetSelfException() {
		super(GlobalResponseCode.INVALID_TARGET_SELF);
	}
}
