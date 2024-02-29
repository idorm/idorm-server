package idorm.idormServer.photo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.PhotoResponseCode;

public class UnsupportedFileTypeException extends BaseException {

  public UnsupportedFileTypeException() {
    super(PhotoResponseCode.UNSUPPORTED_FILE_TYPE);
  }
}
