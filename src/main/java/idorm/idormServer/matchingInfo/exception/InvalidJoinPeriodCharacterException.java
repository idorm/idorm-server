package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidJoinPeriodCharacterException extends BaseException {

  public InvalidJoinPeriodCharacterException() {
    super(MatchingInfoResponseCode.INVALID_JOIN_PERIOD_CHARACTER);
  }
}
