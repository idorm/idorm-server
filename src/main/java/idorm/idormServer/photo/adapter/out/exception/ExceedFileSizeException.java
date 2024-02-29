package idorm.idormServer.photo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.PhotoResponseCode;

public class ExceedFileSizeException extends BaseException {

  public ExceedFileSizeException() {
    super(PhotoResponseCode.EXCEED_FILE_SIZE);
  }
}
