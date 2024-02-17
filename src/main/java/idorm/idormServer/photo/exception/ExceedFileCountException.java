package idorm.idormServer.photo.exception;

import idorm.idormServer.common.exception.BaseException;

public class ExceedFileCountException extends BaseException {

  public ExceedFileCountException() {
    super(PhotoResponseCode.EXCEED_FILE_COUNT);
  }
}
