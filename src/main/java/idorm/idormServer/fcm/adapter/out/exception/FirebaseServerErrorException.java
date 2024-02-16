package idorm.idormServer.fcm.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.fcm.adapter.out.FcmResponseCode;

public class FirebaseServerErrorException extends BaseException {

  public FirebaseServerErrorException() {
    super(FcmResponseCode.FIREBASE_SERER_ERROR);
  }
}
