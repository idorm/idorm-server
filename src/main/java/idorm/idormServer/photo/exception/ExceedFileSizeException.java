package idorm.idormServer.photo.exception;

import idorm.idormServer.common.exception.BaseException;

public class ExceedFileSizeException extends BaseException {

  public ExceedFileSizeException() {
    super(PhotoResponseCode.EXCEED_FILE_SIZE);
  }
}
