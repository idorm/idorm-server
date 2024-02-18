package idorm.idormServer.photo.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.api.PhotoResponseCode;

public class NotFoundFileException extends BaseException {

	public NotFoundFileException() {
		super(PhotoResponseCode.NOT_FOUND_FILE);
	}
}
