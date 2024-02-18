package idorm.idormServer.photo.adapter.out.api.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.photo.adapter.out.api.PhotoResponseCode;

public class ExceedFileCountException extends BaseException {

  public ExceedFileCountException() {
    super(PhotoResponseCode.EXCEED_FILE_COUNT);
  }
}