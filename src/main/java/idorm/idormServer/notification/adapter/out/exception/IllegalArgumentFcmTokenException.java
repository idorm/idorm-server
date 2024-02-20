package idorm.idormServer.notification.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.notification.adapter.out.FcmResponseCode;

public class IllegalArgumentFcmTokenException extends BaseException {

  public IllegalArgumentFcmTokenException() {
    super(FcmResponseCode.ILLEGAL_ARGUMENT_FCM_TOKEN);
  }
}
