package idorm.idormServer.fcm.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.fcm.adapter.out.FcmResponseCode;

public class IllegalArgumentFcmTokenException extends BaseException {

  public IllegalArgumentFcmTokenException() {
    super(FcmResponseCode.ILLEGAL_ARGUMENT_FCM_TOKEN);
  }
}
