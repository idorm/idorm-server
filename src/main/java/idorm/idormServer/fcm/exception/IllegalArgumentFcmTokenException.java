package idorm.idormServer.fcm.exception;

import idorm.idormServer.common.exception.BaseException;

public class IllegalArgumentFcmTokenException extends BaseException {

  public IllegalArgumentFcmTokenException() {
    super(FcmResponseCode.ILLEGAL_ARGUMENT_FCM_TOKEN);
  }
}
