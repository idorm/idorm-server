package idorm.idormServer.photo.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.api.PhotoResponseCode;

public class S3ClientException extends BaseException {

	public S3ClientException() {
		super(PhotoResponseCode.S3_SERVER_ERROR);
	}
}
