package idorm.idormServer.photo.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.api.PhotoResponseCode;

public class UnsupportedFileTypeException extends BaseException {

  public UnsupportedFileTypeException() {
    super(PhotoResponseCode.UNSUPPORTED_FILE_TYPE);
  }
}
