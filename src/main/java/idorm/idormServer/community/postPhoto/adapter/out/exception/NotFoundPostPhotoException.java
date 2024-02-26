package idorm.idormServer.community.postPhoto.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.postPhoto.adapter.out.PostPhotoResponseCode;

public class NotFoundPostPhotoException extends BaseException {

	public NotFoundPostPhotoException() {
		super(PostPhotoResponseCode.NOT_FOUND_POSTPHOTO);
	}
}
