package idorm.idormServer.photo.exception;

import idorm.idormServer.common.exception.BaseException;

public class UnsupportedFileTypeException extends BaseException {

  public UnsupportedFileTypeException() {
    super(PhotoResponseCode.UNSUPPORTED_FILE_TYPE);
  }
}
