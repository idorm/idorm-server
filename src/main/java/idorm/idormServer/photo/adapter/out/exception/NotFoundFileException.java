package idorm.idormServer.photo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.PhotoResponseCode;

public class NotFoundFileException extends BaseException {

	public NotFoundFileException() {
		super(PhotoResponseCode.NOT_FOUND_FILE);
	}
}
