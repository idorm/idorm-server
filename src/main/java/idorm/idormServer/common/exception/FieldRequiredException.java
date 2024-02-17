package idorm.idormServer.common.exception;

public class FieldRequiredException extends BaseException {

  public FieldRequiredException() {
    super(GlobalResponseCode.FILED_REQUIRED);
  }
}
