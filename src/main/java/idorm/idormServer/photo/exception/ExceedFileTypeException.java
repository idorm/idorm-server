package idorm.idormServer.photo.exception;

import idorm.idormServer.common.exception.BaseException;

public class ExceedFileTypeException extends BaseException {

  public ExceedFileTypeException() {
    super(PhotoResponseCode.EXCEED_FILE_TYPE);
  }
}
