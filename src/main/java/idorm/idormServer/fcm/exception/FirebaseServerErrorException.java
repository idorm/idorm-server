package idorm.idormServer.fcm.exception;

import idorm.idormServer.common.exception.BaseException;

public class FirebaseServerErrorException extends BaseException {

  public FirebaseServerErrorException() {
    super(FcmResponseCode.FIREBASE_SERER_ERROR);
  }
}
