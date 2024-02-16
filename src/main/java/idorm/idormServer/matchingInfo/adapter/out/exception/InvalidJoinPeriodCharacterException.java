package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidJoinPeriodCharacterException extends BaseException {

  public InvalidJoinPeriodCharacterException() {
    super(MatchingInfoResponseCode.INVALID_JOIN_PERIOD_CHARACTER);
  }
}
